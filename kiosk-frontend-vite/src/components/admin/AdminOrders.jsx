import React, { useEffect, useState } from 'react';
import axios from 'axios';
import OrderDetail from './OrderDetail';
import SockJS from 'sockjs-client';
import Stomp from 'stompjs';

const AdminOrders = () => {
  const [orders, setOrders] = useState([]);
  const [selectedOrderId, setSelectedOrderId] = useState(null);

  useEffect(() => {
    const socket = new SockJS('http://localhost:8081/ws-endpoint');
    const client = Stomp.over(socket);
    client.debug = console.log;
    console.log('🔥 AdminOrders 컴포넌트 렌더링됨');

    // WebSocket 연결 후 구독
    client.connect(
      {},
      () => {
        console.log('📡 WebSocket 연결 성공 ✅');

        client.subscribe('/topic/orders', (message) => {
          const newOrder = JSON.parse(message.body);
          console.log('🆕 새 주문 수신:', newOrder);
          setOrders((prevOrders) => [newOrder, ...prevOrders]);
        });
      },
      (error) => {
        console.error('❌ WebSocket 연결 실패:', error);
      }
    );

    return () => {
      // WebSocket 연결 해제
      if (client) {
        client.disconnect(() => {
          console.log('🛑 WebSocket 연결 해제됨');
        });
      }
    };
  }, []);

  // 주문 목록 API 호출
  useEffect(() => {
    const url = 'http://localhost:8081/api/admin/orders';
    axios
      .get(url)
      .then((response) => {
        setOrders(response.data);
      })
      .catch((error) => {
        console.error('❌ 주문 목록 불러오기 실패:', error);
      });
  }, []);

  // 상태 변경 (예: "완료"로 변경)
  const changeOrderStatus = (orderId) => {
    // 보내는 데이터 수정 (status를 request body로 변경)
    const data = { status: 'COMPLETED' };

    axios
      .patch(`http://localhost:8081/api/admin/orders/${orderId}/status`, data)
      .then((response) => {
        // 상태 변경 후, 주문 목록을 다시 로드
        console.log('주문 상태가 완료로 변경되었습니다.', response.data);
        setOrders((prevOrders) =>
          prevOrders.map((order) =>
            order.id === orderId ? { ...order, status: 'COMPLETED' } : order
          )
        );
      })
      .catch((error) => {
        console.error('❌ 상태 변경 실패:', error);
      });
  };

  return (
    <div className="p-4">
      <h1 className="text-2xl font-bold mb-4">주문 관리 ✅ 연결 성공</h1>
      <table className="w-full table-auto border-collapse border border-gray-300">
        <thead>
          <tr className="bg-gray-100">
            <th className="border p-2">주문번호</th>
            <th className="border p-2">메뉴</th>
            <th className="border p-2">수량</th>
            <th className="border p-2">상태</th>
            <th className="border p-2">상세보기</th>
          </tr>
        </thead>
        <tbody>
          {orders.map((order) => (
            <tr key={order.id}>
              <td className="border p-2">{order.id}</td>
              <td className="border p-2">
                {order.items?.map((item, index) => (
                  <div key={index}>{item.menuName}</div>
                ))}
              </td>
              <td className="border p-2">
                {order.items?.map((item, index) => (
                  <div key={index}>{item.quantity}</div>
                ))}
              </td>
              <td className="border p-2">{order.status}</td>
              <td className="border p-2">
                <button
                  onClick={() => setSelectedOrderId(order.id)} // 주문 ID를 선택
                  className="text-blue-600 underline"
                >
                  상세보기
                </button>
                {order.status !== 'COMPLETED' && (
                  <button
                    onClick={() => changeOrderStatus(order.id)}
                    className="text-green-600 underline ml-2"
                  >
                    완료로 변경
                  </button>
                )}
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      {/* 주문 상세보기 */}
      {selectedOrderId && (
        <OrderDetail
          orderId={selectedOrderId}
          onClose={() => setSelectedOrderId(null)} // 닫기 버튼 클릭 시
        />
      )}
    </div>
  );
};

export default AdminOrders;
