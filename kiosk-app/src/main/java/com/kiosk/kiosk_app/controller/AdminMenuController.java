package com.kiosk.kiosk_app.controller;

import com.kiosk.kiosk_app.dto.MenuRequest;
import com.kiosk.kiosk_app.dto.MenuResponse;
import com.kiosk.kiosk_app.service.AdminMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/menus") // 유지
@RequiredArgsConstructor
public class AdminMenuController {

    private final AdminMenuService adminMenuService;

    @GetMapping
    public List<MenuResponse> getAllMenus() {
        return adminMenuService.getAllMenus();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MenuResponse> getMenuById(@PathVariable Long id) {
        return ResponseEntity.ok(adminMenuService.getMenuById(id));
    }

    @PostMapping
    public ResponseEntity<Void> addMenu(@RequestBody MenuRequest request) {
        adminMenuService.addMenu(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMenu(@PathVariable Long id) {
        adminMenuService.deleteMenu(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateMenu(@PathVariable Long id, @RequestBody MenuRequest request) {
        adminMenuService.updateMenu(id, request);
        return ResponseEntity.noContent().build();
    }
}
