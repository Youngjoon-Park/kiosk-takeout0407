package com.kiosk.kiosk_app.controller;

import com.kiosk.kiosk_app.dto.OrderDetailResponse;
import com.kiosk.kiosk_app.dto.OrderDto;
import com.kiosk.kiosk_app.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/admin")
public class OrderAdminController {

    private final OrderService orderService;

    public OrderAdminController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/orders")
    public List<OrderDto> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/orders/{orderId}")
    public OrderDetailResponse getOrderDetail(@PathVariable Long orderId) {
        return orderService.getOrderDetail(orderId);
    }

    @PatchMapping("/orders/{orderId}/status")
    public ResponseEntity<Void> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestBody String status) { // status를 String으로 받음
        // 상태값을 그대로 사용하고 상태 업데이트
        orderService.updateOrderStatus(orderId, status); // String을 그대로 사용
        return ResponseEntity.ok().build();
    }

}
