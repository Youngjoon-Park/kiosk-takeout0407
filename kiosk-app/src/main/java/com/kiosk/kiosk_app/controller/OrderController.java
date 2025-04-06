package com.kiosk.kiosk_app.controller;

import com.kiosk.kiosk_app.domain.Order;
import com.kiosk.kiosk_app.domain.Menu;
import com.kiosk.kiosk_app.domain.OrderItem;

import com.kiosk.kiosk_app.dto.*;
import com.kiosk.kiosk_app.repository.MenuRepository;
import com.kiosk.kiosk_app.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/order")
@CrossOrigin(origins = "http://localhost:5173")
public class OrderController {

        @Autowired
        private OrderRepository orderRepository;

        @Autowired
        private MenuRepository menuRepository;

        @Autowired
        private SimpMessagingTemplate messagingTemplate;

        // 주문 생성
        @PostMapping
        @Transactional
        public OrderResponse createOrder(@RequestBody OrderRequest request) {
                Order order = new Order();
                order.setCreatedAt(LocalDateTime.now()); // 주문 시간
                order.setStatus("PENDING"); // 상태를 문자열로 설정

                List<OrderItem> orderItems = new ArrayList<>();
                int totalPrice = 0;
                int totalAmount = 0;

                // 주문 항목 추가
                for (OrderItemRequest item : request.getItems()) {
                        Menu menu = menuRepository.findById(item.getMenuId())
                                        .orElseThrow(() -> new IllegalArgumentException("없는 메뉴 ID"));

                        OrderItem orderItem = new OrderItem();
                        orderItem.setMenu(menu);
                        orderItem.setOrder(order);
                        orderItem.setQuantity(item.getQuantity());
                        orderItem.setPrice(menu.getPrice()); // 가격 설정

                        orderItems.add(orderItem);

                        totalPrice += menu.getPrice() * item.getQuantity(); // 총 금액
                        totalAmount += item.getQuantity(); // 총 수량
                }

                order.setItems(orderItems); // 주문 항목 설정
                order.setTotalPrice(totalPrice); // 총 가격 설정
                order.setTotalAmount(totalAmount); // 총 수량 설정

                orderRepository.save(order); // 주문 저장

                // items 목록을 채워서 반환
                List<OrderResponse.ItemDto> itemDtos = orderItems.stream()
                                .map(orderItem -> new OrderResponse.ItemDto(
                                                orderItem.getMenu().getName(),
                                                orderItem.getQuantity(),
                                                orderItem.getPrice()))
                                .collect(Collectors.toList());

                messagingTemplate.convertAndSend("/topic/orders",
                                new OrderResponse(order.getId(), totalPrice, order.getStatus(), itemDtos));

                return new OrderResponse(order.getId(), totalPrice, order.getStatus(), itemDtos);
        }

        // 주문 목록 조회
        @GetMapping
        public List<OrderResponse> getOrders(@RequestParam(required = false) String status) {
                List<Order> orders;

                if (status != null) {
                        orders = orderRepository.findByStatusOrderByCreatedAtDesc(status);
                } else {
                        orders = orderRepository.findAllByOrderByCreatedAtDesc();
                }

                if (orders == null) {
                        orders = new ArrayList<>();
                }

                return orders.stream()
                                .map(order -> {
                                        List<OrderResponse.ItemDto> itemDtos = order.getItems().stream()
                                                        .map(orderItem -> new OrderResponse.ItemDto(
                                                                        orderItem.getMenu().getName(),
                                                                        orderItem.getQuantity(),
                                                                        orderItem.getPrice()))
                                                        .collect(Collectors.toList());

                                        return new OrderResponse(order.getId(), order.getTotalPrice(),
                                                        order.getStatus(), itemDtos);
                                })
                                .collect(Collectors.toList());
        }

        // 주문 상세 조회
        @GetMapping("/{orderId}")
        public OrderDetailResponse getOrderDetail(@PathVariable Long orderId) {
                Order order = orderRepository.findById(orderId)
                                .orElseThrow(() -> new IllegalArgumentException("해당 주문이 존재하지 않습니다."));

                List<OrderItemDetail> items = order.getItems().stream()
                                .map(item -> new OrderItemDetail(
                                                item.getMenu().getName(),
                                                item.getQuantity(),
                                                item.getMenu().getPrice()))
                                .collect(Collectors.toList());

                return new OrderDetailResponse(order.getId(), order.getTotalPrice(), order.getStatus(), items);
        }

        // 주문 상태 업데이트
        @PatchMapping("/{orderId}/status")
        public OrderResponse updateOrderStatus(@PathVariable Long orderId,
                        @RequestBody OrderStatusUpdateRequest request) {
                String status = request.getStatus(); // 상태를 문자열로 받음
                System.out.println("📦 받은 상태값: " + status);

                Order order = orderRepository.findById(orderId)
                                .orElseThrow(() -> new IllegalArgumentException("해당 주문이 존재하지 않습니다."));

                // 상태를 String으로 처리
                order.setStatus(status);

                orderRepository.save(order);

                List<OrderResponse.ItemDto> itemDtos = order.getItems().stream()
                                .map(orderItem -> new OrderResponse.ItemDto(
                                                orderItem.getMenu().getName(),
                                                orderItem.getQuantity(),
                                                orderItem.getPrice()))
                                .collect(Collectors.toList());

                return new OrderResponse(order.getId(), order.getTotalPrice(), order.getStatus(), itemDtos);
        }
}
