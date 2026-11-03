"""请假审批引擎的单元测试。

运行：python3 -m unittest test_engine.py
"""

from __future__ import annotations

import json
import tempfile
import unittest
from pathlib import Path

from engine import ApprovalEngine
from model import (
    InvalidStateError,
    Node,
    NotAuthorizedError,
    NotFoundError,
    Status,
    ValidationError,
)
from store import ApprovalStore, StoreError

SUPERVISOR = "王经理"
HR = "李人事"
APPLICANT = "张三"


class EngineTestCase(unittest.TestCase):
    def setUp(self) -> None:
        self._tmp = tempfile.TemporaryDirectory()
        self.addCleanup(self._tmp.cleanup)
        self.path = Path(self._tmp.name) / "approvals.json"
        self.engine = ApprovalEngine(self.path, supervisor=SUPERVISOR, hr=HR)

    def submit(self, days: float = 1, leave_type: str = "事假", applicant: str = APPLICANT):
        return self.engine.submit(applicant, days, leave_type)

    def read_json(self) -> dict:
        return json.loads(self.path.read_text(encoding="utf-8"))

    # 事假 1 天：主管通过即结束 -----------------------------------------

    def test_personal_leave_one_day_ends_after_supervisor(self) -> None:
        created = self.submit(1, "事假")
        request_id = created["request_id"]

        self.assertEqual(created["current_node"], Node.SUPERVISOR.value)
        self.assertEqual(created["status"], Status.PENDING.value)
        self.assertEqual(created["current_approver"], SUPERVISOR)
        self.assertFalse(created["requires_countersign"])
        self.assertEqual(created["approvals"], [])

        result = self.engine.approve(request_id, SUPERVISOR)

        self.assertEqual(result["current_node"], Node.END.value)
        self.assertEqual(result["status"], Status.FINISHED.value)
        self.assertIsNone(result["current_approver"])
        self.assertEqual(
            result["approvals"],
            [{"node": "主管审批", "approver": SUPERVISOR, "decision": "通过"}],
        )

        stored = self.read_json()[request_id]
        self.assertEqual(stored["current_node"], Node.END.value)
        self.assertEqual(stored["status"], Status.FINISHED.value)
        self.assertEqual(stored["approvals"], result["approvals"])

    def test_personal_leave_more_days_still_ends_after_supervisor(self) -> None:
        created = self.submit(5, "事假")
        result = self.engine.approve(created["request_id"], SUPERVISOR)
        self.assertEqual(result["status"], Status.FINISHED.value)
        self.assertEqual(result["current_node"], Node.END.value)
        self.assertFalse(result["requires_countersign"])

    # 病假会签 ---------------------------------------------------------

    def test_sick_leave_two_days_stays_in_countersign_after_supervisor(self) -> None:
        created = self.submit(2, "病假")
        request_id = created["request_id"]
        self.assertTrue(created["requires_countersign"])

        result = self.engine.approve(request_id, SUPERVISOR)

        self.assertEqual(result["current_node"], Node.HR.value)
        self.assertEqual(result["status"], Status.PENDING.value)
        self.assertEqual(result["current_approver"], HR)
        self.assertEqual(len(result["approvals"]), 1)

        stored = self.read_json()[request_id]
        self.assertEqual(stored["current_node"], Node.HR.value)
        self.assertEqual(stored["status"], Status.PENDING.value)

    def test_sick_leave_two_days_ends_after_both_approvals(self) -> None:
        created = self.submit(2, "病假")
        request_id = created["request_id"]

        self.engine.approve(request_id, SUPERVISOR)
        result = self.engine.approve(request_id, HR)

        self.assertEqual(result["status"], Status.FINISHED.value)
        self.assertEqual(result["current_node"], Node.END.value)
        self.assertEqual(
            result["approvals"],
            [
                {"node": "主管审批", "approver": SUPERVISOR, "decision": "通过"},
                {"node": "人事审批", "approver": HR, "decision": "通过"},
            ],
        )

        stored = self.read_json()[request_id]
        self.assertEqual(stored["status"], Status.FINISHED.value)
        self.assertEqual(len(stored["approvals"]), 2)

    def test_sick_leave_one_day_does_not_need_countersign(self) -> None:
        created = self.submit(1, "病假")
        self.assertFalse(created["requires_countersign"])
        result = self.engine.approve(created["request_id"], SUPERVISOR)
        self.assertEqual(result["status"], Status.FINISHED.value)
        self.assertEqual(result["current_node"], Node.END.value)

    # 权限 -------------------------------------------------------------

    def test_non_current_approver_cannot_approve_or_reject(self) -> None:
        created = self.submit(1, "事假")
        request_id = created["request_id"]
        before = self.path.read_bytes()

        with self.assertRaises(NotAuthorizedError):
            self.engine.approve(request_id, HR)
        with self.assertRaises(NotAuthorizedError):
            self.engine.reject(request_id, APPLICANT)
        with self.assertRaises(NotAuthorizedError):
            self.engine.approve(request_id, "路人甲")

        self.assertEqual(self.path.read_bytes(), before)
        self.assertEqual(self.engine.status(request_id)["status"], Status.PENDING.value)

    def test_supervisor_cannot_act_when_hr_node_is_current(self) -> None:
        created = self.submit(2, "病假")
        request_id = created["request_id"]
        self.engine.approve(request_id, SUPERVISOR)
        before = self.path.read_bytes()

        with self.assertRaises(NotAuthorizedError):
            self.engine.approve(request_id, SUPERVISOR)
        with self.assertRaises(NotAuthorizedError):
            self.engine.reject(request_id, SUPERVISOR)

        self.assertEqual(self.path.read_bytes(), before)
        self.assertEqual(self.engine.status(request_id)["current_node"], Node.HR.value)

    # 驳回 -------------------------------------------------------------

    def test_reject_returns_to_submit_and_keeps_previous_records(self) -> None:
        created = self.submit(2, "病假")
        request_id = created["request_id"]
        self.engine.approve(request_id, SUPERVISOR)

        result = self.engine.reject(request_id, HR)

        expected_records = [
            {"node": "主管审批", "approver": SUPERVISOR, "decision": "通过"},
            {"node": "人事审批", "approver": HR, "decision": "驳回"},
        ]
        self.assertEqual(result["current_node"], Node.SUBMITTED.value)
        self.assertEqual(result["status"], Status.REJECTED.value)
        self.assertIsNone(result["current_approver"])
        self.assertEqual(result["approvals"], expected_records)

        stored = self.read_json()[request_id]
        self.assertEqual(stored["current_node"], Node.SUBMITTED.value)
        self.assertEqual(stored["status"], Status.REJECTED.value)
        self.assertEqual(stored["approvals"], expected_records)

    def test_reject_at_supervisor_node_keeps_reject_record(self) -> None:
        created = self.submit(1, "事假")
        request_id = created["request_id"]

        result = self.engine.reject(request_id, SUPERVISOR)

        self.assertEqual(result["current_node"], Node.SUBMITTED.value)
        self.assertEqual(result["status"], Status.REJECTED.value)
        self.assertEqual(
            result["approvals"],
            [{"node": "主管审批", "approver": SUPERVISOR, "decision": "驳回"}],
        )

    # 结束 / 驳回后不能再操作，且文件不变 ---------------------------------

    def test_finished_request_cannot_be_approved_or_rejected(self) -> None:
        created = self.submit(1, "事假")
        request_id = created["request_id"]
        self.engine.approve(request_id, SUPERVISOR)
        before = self.path.read_bytes()

        with self.assertRaises(InvalidStateError):
            self.engine.approve(request_id, SUPERVISOR)
        with self.assertRaises(InvalidStateError):
            self.engine.reject(request_id, SUPERVISOR)
        with self.assertRaises(InvalidStateError):
            self.engine.approve(request_id, HR)

        self.assertEqual(self.path.read_bytes(), before)
        self.assertEqual(self.engine.status(request_id)["status"], Status.FINISHED.value)

    def test_rejected_request_cannot_be_approved_or_rejected(self) -> None:
        created = self.submit(1, "事假")
        request_id = created["request_id"]
        self.engine.reject(request_id, SUPERVISOR)
        before = self.path.read_bytes()

        with self.assertRaises(InvalidStateError):
            self.engine.approve(request_id, SUPERVISOR)
        with self.assertRaises(InvalidStateError):
            self.engine.reject(request_id, SUPERVISOR)

        self.assertEqual(self.path.read_bytes(), before)
        self.assertEqual(self.engine.status(request_id)["status"], Status.REJECTED.value)

    # 存储结构 ---------------------------------------------------------

    def test_store_keeps_exactly_one_current_node_per_request(self) -> None:
        created = self.submit(2, "病假")
        request_id = created["request_id"]

        stored = self.read_json()[request_id]
        self.assertIsInstance(stored["current_node"], str)
        self.assertNotIn("current_nodes", stored)

        self.engine.approve(request_id, SUPERVISOR)
        stored = self.read_json()[request_id]
        self.assertEqual(stored["current_node"], Node.HR.value)
        self.assertEqual(len(stored["approvals"]), 1)

    # 输入校验 ---------------------------------------------------------

    def test_submit_validation_errors_do_not_create_file(self) -> None:
        for applicant, days, leave_type in [
            ("", 1, "事假"),
            ("张三", 0, "事假"),
            ("张三", -1, "病假"),
            ("张三", 1, "婚假"),
            ("张三", "一天", "事假"),
        ]:
            with self.subTest(applicant=applicant, days=days, leave_type=leave_type):
                with self.assertRaises(ValidationError):
                    self.engine.submit(applicant, days, leave_type)
        self.assertFalse(self.path.exists())

    def test_duplicate_request_id_is_rejected_without_touching_file(self) -> None:
        created = self.submit(1, "事假")
        before = self.path.read_bytes()

        with self.assertRaises(ValidationError):
            self.engine.submit(APPLICANT, 1, "事假", request_id=created["request_id"])

        self.assertEqual(self.path.read_bytes(), before)

    def test_unknown_request_raises_not_found(self) -> None:
        with self.assertRaises(NotFoundError):
            self.engine.status("LR-9999")
        with self.assertRaises(NotFoundError):
            self.engine.approve("LR-9999", SUPERVISOR)
        with self.assertRaises(NotFoundError):
            self.engine.reject("LR-9999", SUPERVISOR)


class StoreTestCase(unittest.TestCase):
    def test_store_round_trip(self) -> None:
        with tempfile.TemporaryDirectory() as tmp:
            path = Path(tmp) / "approvals.json"
            store = ApprovalStore(path)
            self.assertEqual(store.load(), {})

            record = {
                "applicant": "张三",
                "days": 1.0,
                "leave_type": "事假",
                "supervisor": SUPERVISOR,
                "hr": HR,
                "current_node": "主管审批",
                "status": "审批中",
                "approvals": [],
            }
            store.put("LR-0001", record)

            self.assertEqual(store.get("LR-0001"), record)
            self.assertEqual(
                json.loads(path.read_text(encoding="utf-8")), {"LR-0001": record}
            )

    def test_store_rejects_multiple_current_nodes(self) -> None:
        with tempfile.TemporaryDirectory() as tmp:
            path = Path(tmp) / "approvals.json"
            path.write_text(
                json.dumps(
                    {
                        "LR-0001": {
                            "current_node": ["主管审批", "人事审批"],
                            "approvals": [],
                        }
                    }
                ),
                encoding="utf-8",
            )
            with self.assertRaises(StoreError):
                ApprovalStore(path).load()


if __name__ == "__main__":
    unittest.main()
