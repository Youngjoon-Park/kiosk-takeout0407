package com.kiosk.kiosk_app.controller;

import com.kiosk.kiosk_app.domain.Menu;
import com.kiosk.kiosk_app.repository.MenuRepository;

import jakarta.annotation.PostConstruct;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/menus") // ✅ 사용자 전용 경로
public class MenuController {

    private final MenuRepository menuRepository;

    public MenuController(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
        System.out.println("✅ MenuController 생성됨!"); // 여기에 로그
    }

    @GetMapping
    public List<Menu> list() {
        System.out.println("🔥 /api/menus 요청 도착!");
        return menuRepository.findByEnabledTrue(); // 사용자용: 활성 메뉴만
    }

    @PostConstruct
    public void init() {
        System.out.println("✅ MenuController 로딩됨");
    }

}
