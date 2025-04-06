package com.kiosk.kiosk_app.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class OrderDetailResponse {
    private Long id;
    private Integer totalPrice;
    private String status; // OrderStatus 대신 String 타입으로 변경
    private List<OrderItemDetail> items;

    // Constructor, getters, and setters
    public OrderDetailResponse(Long id, Integer totalPrice, String status, List<OrderItemDetail> items) {
        this.id = id;
        this.totalPrice = totalPrice;
        this.status = status;
        this.items = items;
    }

    // Getters and setters
}

// @Getter
// @NoArgsConstructor
// @AllArgsConstructor
// @Builder
// public class OrderDetailResponse {
// private Long orderId;
// private int totalPrice;
// private OrderStatus status;
// private List<OrderItemDetail> items;
// }
