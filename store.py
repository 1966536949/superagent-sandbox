"""把请假单、当前节点和审批记录保存到 approvals.json。

文件结构是以请假单号为键的 JSON 对象，例如::

    {
      "LR-0001": {
        "applicant": "张三",
        "days": 1.0,
        "leave_type": "事假",
        "supervisor": "王经理",
        "hr": "李人事",
        "current_node": "主管审批",
        "status": "审批中",
        "approvals": []
      }
    }

current_node 是单值字段，因此同一张单子在任一时刻只有一个当前节点。
写入先落到临时文件再原子替换，避免写到一半留下损坏的 approvals.json。

只使用 Python 标准库。
"""

from __future__ import annotations

import json
import os
import tempfile
from pathlib import Path
from typing import Any

DEFAULT_PATH = "approvals.json"


class StoreError(Exception):
    """approvals.json 无法读取、解析或写入。"""


class ApprovalStore:
    """approvals.json 的读写封装。"""

    def __init__(self, path: str | os.PathLike[str] = DEFAULT_PATH) -> None:
        self.path = Path(path)

    def load(self) -> dict[str, dict[str, Any]]:
        """读取全部请假单；文件不存在或为空时返回空字典。"""
        try:
            raw = self.path.read_text(encoding="utf-8")
        except FileNotFoundError:
            return {}
        except OSError as exc:
            raise StoreError(f"读取 {self.path} 失败：{exc}") from exc

        if not raw.strip():
            return {}

        try:
            data = json.loads(raw)
        except json.JSONDecodeError as exc:
            raise StoreError(f"{self.path} 不是合法的 JSON：{exc}") from exc

        if not isinstance(data, dict):
            raise StoreError(f"{self.path} 的顶层结构必须是 JSON 对象")

        for request_id, record in data.items():
            self._validate_record(request_id, record)
        return data

    def save(self, records: dict[str, dict[str, Any]]) -> None:
        """原子地写入全部请假单。"""
        if not isinstance(records, dict):
            raise StoreError("approvals.json 的顶层结构必须是 JSON 对象")

        try:
            payload = json.dumps(records, ensure_ascii=False, indent=2, sort_keys=True) + "\n"
        except (TypeError, ValueError) as exc:
            raise StoreError(f"无法序列化审批数据：{exc}") from exc

        parent = self.path.parent
        try:
            parent.mkdir(parents=True, exist_ok=True)
        except OSError as exc:
            raise StoreError(f"创建目录 {parent} 失败：{exc}") from exc

        fd = -1
        tmp_path: Path | None = None
        try:
            fd, tmp_name = tempfile.mkstemp(
                dir=str(parent), prefix=f".{self.path.name}.", suffix=".tmp"
            )
            tmp_path = Path(tmp_name)
            with os.fdopen(fd, "w", encoding="utf-8") as handle:
                fd = -1
                handle.write(payload)
                handle.flush()
                os.fsync(handle.fileno())
            os.replace(tmp_path, self.path)
            tmp_path = None
        except OSError as exc:
            raise StoreError(f"写入 {self.path} 失败：{exc}") from exc
        finally:
            if fd != -1:
                os.close(fd)
            if tmp_path is not None:
                tmp_path.unlink(missing_ok=True)

    def get(self, request_id: str) -> dict[str, Any] | None:
        """读取一张请假单。"""
        return self.load().get(request_id)

    def put(self, request_id: str, record: dict[str, Any]) -> None:
        """写入一张请假单，保留文件中的其他请假单。"""
        records = self.load()
        records[request_id] = record
        self.save(records)

    @staticmethod
    def _validate_record(request_id: Any, record: Any) -> None:
        if not isinstance(request_id, str) or not request_id.strip():
            raise StoreError("请假单号必须是非空字符串")
        if not isinstance(record, dict):
            raise StoreError(f"请假单 {request_id} 的记录必须是 JSON 对象")

        current_node = record.get("current_node")
        if not isinstance(current_node, str) or not current_node.strip():
            raise StoreError(f"请假单 {request_id} 必须且只能有一个当前节点")

        approvals = record.get("approvals")
        if not isinstance(approvals, list):
            raise StoreError(f"请假单 {request_id} 缺少审批记录列表")
        if not all(isinstance(item, dict) for item in approvals):
            raise StoreError(f"请假单 {request_id} 的审批记录必须是 JSON 对象")
