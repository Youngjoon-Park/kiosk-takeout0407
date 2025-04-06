package com.kiosk.kiosk_app.repository;

import com.kiosk.kiosk_app.domain.Order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    // String으로 상태 처리
    List<Order> findByStatusOrderByCreatedAtDesc(String status);

    List<Order> findAllByOrderByCreatedAtDesc();
}
