// 📁 src/pages/AdminPayments.jsx
import React, { useEffect, useState } from "react";
import { getAllPayments } from "../api/adminPaymentApi";

const AdminPayments = () => {
  const [payments, setPayments] = useState([]);

  useEffect(() => {
    const fetchPayments = async () => {
      try {
        const data = await getAllPayments();  // getAllPayments 함수 호출
        setPayments(data);
      } catch (error) {
        alert("❌ 결제 내역 불러오기 실패");
      }
    };
    fetchPayments();
  }, []);

  return (
    <div>
      <h2>💳 결제 내역</h2>
      <table border="1" cellPadding="8">
        <thead>
          <tr>
            <th>ID</th>
            <th>주문번호</th>
            <th>TID</th>
            <th>상태</th>
            <th>승인시간</th>
          </tr>
        </thead>
        <tbody>
          {payments.map(payment => (
            <tr key={payment.id}>
              <td>{payment.id}</td>
              <td>{payment.orderId}</td>
              <td>{payment.tid}</td>
              <td>{payment.status}</td>
              <td>{payment.approvedAt.replace("T", " ").substring(0, 19)}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default AdminPayments;
