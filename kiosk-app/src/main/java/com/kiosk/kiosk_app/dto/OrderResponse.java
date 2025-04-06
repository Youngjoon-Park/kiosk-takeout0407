package com.kiosk.kiosk_app.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderResponse {

    private Long id;
    private int totalPrice;
    private String status; // String 타입으로 수정
    private List<ItemDto> items;

    public OrderResponse(Long id, int totalPrice, String status, List<ItemDto> items) {
        this.id = id;
        this.totalPrice = totalPrice;
        this.status = status; // String으로 처리
        this.items = items;
    }

    @Data
    public static class ItemDto {
        private String name;
        private int quantity;
        private int price;

        public ItemDto(String name, int quantity, int price) {
            this.name = name;
            this.quantity = quantity;
            this.price = price;
        }
    }
}
