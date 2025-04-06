package com.kiosk.kiosk_app.service;

import com.kiosk.kiosk_app.domain.Menu;
import com.kiosk.kiosk_app.dto.MenuRequest;
import com.kiosk.kiosk_app.dto.MenuResponse;
import com.kiosk.kiosk_app.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminMenuService {

    private final MenuRepository menuRepository;

    public List<MenuResponse> getAllMenus() {
        return menuRepository.findByEnabledTrue().stream()
                .map(MenuResponse::from)
                .collect(Collectors.toList());
    }

    public void addMenu(MenuRequest request) {
        Menu menu = Menu.builder()
                .name(request.getName())
                .price(request.getPrice())
                .description(request.getDescription())
                .build();

        menuRepository.save(menu);
    }

    public MenuResponse getMenuById(Long id) {
        Menu menu = menuRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("메뉴를 찾을 수 없습니다."));
        return MenuResponse.from(menu);
    }

    public void updateMenu(Long id, MenuRequest request) {
        Menu menu = menuRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("메뉴가 없습니다"));
        menu.setName(request.getName());
        menu.setPrice(request.getPrice());
        menu.setDescription(request.getDescription());
        menuRepository.save(menu);
    }

    public void deleteMenu(Long id) {
        Menu menu = menuRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("메뉴를 찾을 수 없습니다."));
        menu.setEnabled(false); // ✅ 실제 삭제 대신 숨김 처리 (soft delete)
        menuRepository.save(menu);
    }

}
