"""请假审批引擎的数据模型。

流程节点按顺序为：提交 -> 主管审批 -> 人事审批 -> 结束。
病假超过 1 天时，主管审批和人事审批需要会签；其他情况主管审批通过后
直接进入结束。

只使用 Python 标准库。
"""

from __future__ import annotations

import math
from dataclasses import dataclass, field
from enum import Enum
from typing import Any


class LeaveApprovalError(Exception):
    """请假审批引擎所有异常的基类。"""


class ValidationError(LeaveApprovalError):
    """输入数据不合法。"""


class NotFoundError(LeaveApprovalError):
    """请假单不存在。"""


class NotAuthorizedError(LeaveApprovalError):
    """操作人不是当前节点的审批人。"""


class InvalidStateError(LeaveApprovalError):
    """当前状态不允许该操作。"""


class LeaveType(str, Enum):
    """请假类型。"""

    PERSONAL = "事假"
    SICK = "病假"

    @classmethod
    def coerce(cls, value: Any) -> "LeaveType":
        """把中文值、英文别名或枚举值统一成 LeaveType。"""
        if isinstance(value, cls):
            return value
        if isinstance(value, str):
            alias = _LEAVE_TYPE_ALIASES.get(value.strip().lower())
            if alias is not None:
                return alias
        raise ValidationError("请假类型必须是事假或病假")


_LEAVE_TYPE_ALIASES: dict[str, LeaveType] = {
    "事假": LeaveType.PERSONAL,
    "personal": LeaveType.PERSONAL,
    "personal leave": LeaveType.PERSONAL,
    "病假": LeaveType.SICK,
    "sick": LeaveType.SICK,
    "sick leave": LeaveType.SICK,
}


class Node(str, Enum):
    """流程节点。"""

    SUBMITTED = "提交"
    SUPERVISOR = "主管审批"
    HR = "人事审批"
    END = "结束"


class Status(str, Enum):
    """请假单状态。"""

    PENDING = "审批中"
    REJECTED = "已驳回"
    FINISHED = "已结束"


class Decision(str, Enum):
    """审批决定。"""

    APPROVED = "通过"
    REJECTED = "驳回"


NODE_ORDER: tuple[Node, ...] = (
    Node.SUBMITTED,
    Node.SUPERVISOR,
    Node.HR,
    Node.END,
)

COUNTERSIGN_THRESHOLD_DAYS = 1


def normalize_text(value: Any, label: str) -> str:
    """校验并清理一个非空字符串字段。"""
    if not isinstance(value, str):
        raise ValidationError(f"{label}必须是字符串")
    text = value.strip()
    if not text:
        raise ValidationError(f"{label}不能为空")
    return text


def normalize_days(value: Any) -> float:
    """校验请假天数，返回大于 0 的浮点数。"""
    if isinstance(value, bool) or not isinstance(value, (int, float)):
        raise ValidationError("请假天数必须是数字")
    days = float(value)
    if not math.isfinite(days) or days <= 0:
        raise ValidationError("请假天数必须是大于 0 的有限数字")
    return days


def requires_countersign(leave_type: Any, days: Any) -> bool:
    """病假超过 1 天时需要主管和人事会签。"""
    return (
        LeaveType.coerce(leave_type) is LeaveType.SICK
        and normalize_days(days) > COUNTERSIGN_THRESHOLD_DAYS
    )


def next_node_after_approval(node: Node, leave_type: Any, days: Any) -> Node:
    """当前节点审批通过后应进入的节点。"""
    try:
        node = Node(node)
    except ValueError as exc:
        raise InvalidStateError(f"节点 {node!r} 不能审批") from exc

    if node is Node.SUPERVISOR:
        if requires_countersign(leave_type, days):
            return Node.HR
        return Node.END
    if node is Node.HR:
        return Node.END
    raise InvalidStateError(f"节点 {node.value} 不能审批")


@dataclass(frozen=True)
class ApprovalRecord:
    """一条审批记录。"""

    node: Node
    approver: str
    decision: Decision

    def to_dict(self) -> dict[str, str]:
        return {
            "node": self.node.value,
            "approver": self.approver,
            "decision": self.decision.value,
        }

    @classmethod
    def from_dict(cls, data: Any) -> "ApprovalRecord":
        if not isinstance(data, dict):
            raise ValidationError("审批记录必须是 JSON 对象")
        try:
            node = Node(data["node"])
            approver = normalize_text(data["approver"], "审批人")
            decision = Decision(data["decision"])
        except KeyError as exc:
            raise ValidationError(f"审批记录缺少字段：{exc.args[0]}") from exc
        except ValueError as exc:
            raise ValidationError(f"审批记录包含非法值：{exc}") from exc
        return cls(node=node, approver=approver, decision=decision)


@dataclass
class LeaveRequest:
    """一张请假单及其流程状态。"""

    request_id: str
    applicant: str
    days: float
    leave_type: LeaveType
    supervisor: str
    hr: str
    current_node: Node = Node.SUPERVISOR
    status: Status = Status.PENDING
    approvals: list[ApprovalRecord] = field(default_factory=list)

    @property
    def current_approver(self) -> str | None:
        """当前节点的审批人；没有待审批节点时返回 None。"""
        if self.status is not Status.PENDING:
            return None
        if self.current_node is Node.SUPERVISOR:
            return self.supervisor
        if self.current_node is Node.HR:
            return self.hr
        return None

    @property
    def needs_countersign(self) -> bool:
        return requires_countersign(self.leave_type, self.days)

    def to_record(self) -> dict[str, Any]:
        """转换为写入 approvals.json 的记录。"""
        return {
            "applicant": self.applicant,
            "days": self.days,
            "leave_type": self.leave_type.value,
            "supervisor": self.supervisor,
            "hr": self.hr,
            "current_node": self.current_node.value,
            "status": self.status.value,
            "approvals": [item.to_dict() for item in self.approvals],
        }

    def to_status(self) -> dict[str, Any]:
        """转换为 API 返回的完整状态，包含当前审批人。"""
        return {
            "request_id": self.request_id,
            **self.to_record(),
            "current_approver": self.current_approver,
            "requires_countersign": self.needs_countersign,
        }

    @classmethod
    def from_dict(cls, request_id: str, data: Any) -> "LeaveRequest":
        """从 approvals.json 的记录还原请假单。"""
        if not isinstance(data, dict):
            raise ValidationError("请假单记录必须是 JSON 对象")
        try:
            applicant = normalize_text(data["applicant"], "申请人")
            days = normalize_days(data["days"])
            leave_type = LeaveType.coerce(data["leave_type"])
            supervisor = normalize_text(data["supervisor"], "主管")
            hr = normalize_text(data["hr"], "人事")
            current_node = Node(data["current_node"])
            status = Status(data["status"])
            raw_approvals = data["approvals"]
        except KeyError as exc:
            raise ValidationError(f"请假单记录缺少字段：{exc.args[0]}") from exc
        except ValueError as exc:
            raise ValidationError(f"请假单记录包含非法值：{exc}") from exc

        if not isinstance(raw_approvals, list):
            raise ValidationError("审批记录必须是列表")
        approvals = [ApprovalRecord.from_dict(item) for item in raw_approvals]

        return cls(
            request_id=request_id,
            applicant=applicant,
            days=days,
            leave_type=leave_type,
            supervisor=supervisor,
            hr=hr,
            current_node=current_node,
            status=status,
            approvals=approvals,
        )
