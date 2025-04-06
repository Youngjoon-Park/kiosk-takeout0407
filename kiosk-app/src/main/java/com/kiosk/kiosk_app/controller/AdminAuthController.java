package com.kiosk.kiosk_app.controller;

import com.kiosk.kiosk_app.dto.AdminLoginRequest;
import com.kiosk.kiosk_app.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminAuthController {

    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AdminLoginRequest request) {
        // ⚠️ 임시 계정: admin / 1234
        if ("admin".equals(request.getUsername()) && "1234".equals(request.getPassword())) {
            String token = jwtTokenProvider.createToken(request.getUsername(), "ROLE_ADMIN");
            return ResponseEntity.ok(token); // 토큰 문자열 반환
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 실패");
        }
    }
}
