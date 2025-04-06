package com.kiosk.kiosk_app.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_date")
    private LocalDateTime createdAt; // 주문 생성 시간

    @Column(name = "total_price")
    private Integer totalPrice; // 총 금액

    @Column(name = "total_amount") // 총 수량
    private int totalAmount;

    @Column(name = "status")
    private String status; // 상태를 String으로 변경

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @Builder.Default
    private List<OrderItem> items = new ArrayList<>();

    // totalAmount는 주문 항목들의 수량 합계를 계산해서 리턴하는 메서드로 설정
    public int getTotalAmount() {
        return items.stream()
                .mapToInt(OrderItem::getQuantity)
                .sum(); // 각 OrderItem의 수량 합계
    }

    // PrePersist를 사용하여 새로운 주문이 생성될 때 createdAt과 status를 설정
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        if (this.status == null) {
            this.status = "PENDING"; // 상태가 null일 경우 기본값으로 PENDING 설정
        }

        // totalAmount를 계산하여 저장
        this.totalAmount = getTotalAmount();

        // totalPrice를 계산하여 저장 (가격 합계 계산 필요)
        this.totalPrice = items.stream()
                .mapToInt(item -> item.getQuantity() * item.getPrice()) // 가격 * 수량 합계
                .sum();
    }
}
