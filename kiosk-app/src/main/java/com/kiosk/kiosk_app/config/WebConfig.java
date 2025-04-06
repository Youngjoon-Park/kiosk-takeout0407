package com.kiosk.kiosk_app.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebConfig implements WebMvcConfigurer {

        @Override
        public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                                .allowedOrigins("http://localhost:5173")
                                .allowedMethods("*")
                                .allowedHeaders("*")
                                .allowCredentials(true);
        }

        // ✅ React 새로고침 시 404 방지 (assets, api 제외)
        @Override
        public void addViewControllers(ViewControllerRegistry registry) {
                registry.addViewController("/{spring:[a-zA-Z0-9\\-]+}")
                                .setViewName("forward:/index.html");
                registry.addViewController("/**/{spring:[a-zA-Z0-9\\-]+}")
                                .setViewName("forward:/index.html");
        }

        // ✅ React 정적 리소스(.js, .css) 처리
        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
                registry.addResourceHandler("/assets/**")
                                .addResourceLocations("classpath:/static/assets/");
        }
}
