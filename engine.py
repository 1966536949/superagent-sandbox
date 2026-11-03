"""请假审批引擎。

对外提供 submit、approve、reject、status 四个操作：

* submit  创建请假单并进入主管审批；
* approve 当前节点审批人通过；
* reject  当前节点审批人驳回，回到提交并保留历史审批记录；
* status  查看请假单当前状态。

只有当前节点的审批人可以操作。已经结束或已经驳回的单子再审批会失败，
并且不会改动 approvals.json。

只使用 Python 标准库。
"""

from __future__ import annotations

from typing import Any

from model import (
    ApprovalRecord,
    Decision,
    InvalidStateError,
    LeaveRequest,
    LeaveType,
    Node,
    NotFoundError,
    NotAuthorizedError,
    Status,
    ValidationError,
    next_node_after_approval,
    normalize_days,
    normalize_text,
)
from store import DEFAULT_PATH, ApprovalStore


class ApprovalEngine:
    """基于 approvals.json 的请假审批引擎。"""

    def __init__(
        self,
        path: str = DEFAULT_PATH,
        *,
        supervisor: str = "主管",
        hr: str = "人事",
    ) -> None:
        self.store = ApprovalStore(path)
        self.supervisor = normalize_text(supervisor, "主管")
        self.hr = normalize_text(hr, "人事")

    def submit(
        self,
        applicant: Any,
        days: Any,
        leave_type: Any,
        request_id: str | None = None,
    ) -> dict[str, Any]:
        """创建一张请假单，当前节点为主管审批。"""
        applicant = normalize_text(applicant, "申请人")
        days = normalize_days(days)
        leave_type = LeaveType.coerce(leave_type)

        records = self.store.load()
        if request_id is None:
            request_id = self._next_request_id(records)
        else:
            request_id = normalize_text(request_id, "请假单号")
            if request_id in records:
                raise ValidationError(f"请假单号 {request_id} 已存在")

        request = LeaveRequest(
            request_id=request_id,
            applicant=applicant,
            days=days,
            leave_type=leave_type,
            supervisor=self.supervisor,
            hr=self.hr,
        )
        records[request_id] = request.to_record()
        self.store.save(records)
        return request.to_status()

    def approve(self, request_id: Any, approver: Any) -> dict[str, Any]:
        """当前节点审批人通过；会签未完成时停在下一个审批节点。"""
        return self._decide(request_id, approver, Decision.APPROVED)

    def reject(self, request_id: Any, approver: Any) -> dict[str, Any]:
        """当前节点审批人驳回；单据回到提交并保留已有审批记录。"""
        return self._decide(request_id, approver, Decision.REJECTED)

    def status(self, request_id: Any) -> dict[str, Any]:
        """返回一张请假单的完整状态。"""
        request_id = normalize_text(request_id, "请假单号")
        record = self.store.load().get(request_id)
        if record is None:
            raise NotFoundError(f"请假单 {request_id} 不存在")
        return LeaveRequest.from_dict(request_id, record).to_status()

    def all_status(self) -> list[dict[str, Any]]:
        """按单号顺序返回所有请假单的状态。"""
        records = self.store.load()
        return [
            LeaveRequest.from_dict(request_id, record).to_status()
            for request_id, record in sorted(records.items())
        ]

    def _decide(
        self,
        request_id: Any,
        approver: Any,
        decision: Decision,
    ) -> dict[str, Any]:
        request_id = normalize_text(request_id, "请假单号")
        approver = normalize_text(approver, "审批人")

        records = self.store.load()
        record = records.get(request_id)
        if record is None:
            raise NotFoundError(f"请假单 {request_id} 不存在")

        request = LeaveRequest.from_dict(request_id, record)
        if request.status is not Status.PENDING:
            raise InvalidStateError(
                f"请假单 {request_id} 状态为{request.status.value}，不能再审批"
            )

        expected = request.current_approver
        if expected is None:
            raise InvalidStateError(f"请假单 {request_id} 当前没有待审批节点")
        if approver != expected:
            raise NotAuthorizedError(
                f"当前节点是{request.current_node.value}，审批人是 {expected}，"
                f"{approver} 无权操作"
            )

        request.approvals.append(
            ApprovalRecord(
                node=request.current_node,
                approver=approver,
                decision=decision,
            )
        )

        if decision is Decision.APPROVED:
            next_node = next_node_after_approval(
                request.current_node, request.leave_type, request.days
            )
            request.current_node = next_node
            if next_node is Node.END:
                request.status = Status.FINISHED
        else:
            request.current_node = Node.SUBMITTED
            request.status = Status.REJECTED

        records[request_id] = request.to_record()
        self.store.save(records)
        return request.to_status()

    @staticmethod
    def _next_request_id(records: dict[str, Any]) -> str:
        numbers = [
            int(request_id[3:])
            for request_id in records
            if request_id.startswith("LR-") and request_id[3:].isdigit()
        ]
        number = max(numbers, default=0) + 1
        while f"LR-{number:04d}" in records:
            number += 1
        return f"LR-{number:04d}"


Engine = ApprovalEngine
LeaveApprovalEngine = ApprovalEngine
