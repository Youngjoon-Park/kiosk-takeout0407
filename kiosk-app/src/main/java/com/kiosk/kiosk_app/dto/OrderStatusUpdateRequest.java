// src/main/java/com/kiosk/kiosk_app/dto/OrderStatusUpdateRequest.java
package com.kiosk.kiosk_app.dto;

public class OrderStatusUpdateRequest {
    private String status; // 상태를 String으로 처리

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
