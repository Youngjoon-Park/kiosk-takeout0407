package com.kiosk.kiosk_app.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.kiosk.kiosk_app.domain.Order;
import com.kiosk.kiosk_app.domain.OrderItem; // ✅ 누락된 import 추가

@Data
public class OrderDto {
    private Long id;
    private List<ItemDto> items;
    private int totalAmount;
    private String status; // ✅ String으로 선언
    private LocalDateTime createdAt;

    public static OrderDto fromEntity(Order order) {
        System.out.println("📦 주문 ID: " + order.getId());
        System.out.println("📦 포함된 아이템 수: " + order.getItems().size());

        OrderDto dto = new OrderDto();
        dto.setId(order.getId());
        dto.setItems(order.getItems().stream()
                .map(ItemDto::fromEntity)
                .collect(Collectors.toList()));
        dto.setTotalAmount(order.getTotalAmount());
        dto.setStatus(order.getStatus()); // 이제 String 타입이므로 바로 사용

        dto.setCreatedAt(order.getCreatedAt());
        return dto;
    }

    @Data
    public static class ItemDto {
        private String name;
        private int quantity;

        public static ItemDto fromEntity(OrderItem item) {
            ItemDto dto = new ItemDto();
            dto.setName(item.getMenu().getName());
            dto.setQuantity(item.getQuantity());

            // ✅ item 기준으로 로그 찍기
            System.out.println("📦 아이템 이름: " + dto.getName());
            System.out.println("📦 수량: " + dto.getQuantity());

            return dto;
        }
    }

}
