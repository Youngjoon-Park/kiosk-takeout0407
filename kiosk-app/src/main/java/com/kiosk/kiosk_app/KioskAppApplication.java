package com.kiosk.kiosk_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class KioskAppApplication {

	public static void main(String[] args) {
		System.out.println("✅ 앱 시작됨");
		SpringApplication.run(KioskAppApplication.class, args);
	}

}
