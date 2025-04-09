//package com.example.demo.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//import org.springframework.web.filter.CorsFilter;
//
//@Configuration
//public class CorsConfig {
//
//    @Bean
//    public CorsFilter corsFilter() {
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        CorsConfiguration config = new CorsConfiguration();
//
//        // 必须设置的参数
//        config.setAllowCredentials(true);
//        config.addAllowedOrigin("http://localhost:3030");
//        config.addAllowedHeader("*");
//        config.addAllowedMethod("*");
//        config.setMaxAge(3600L); // 预检请求缓存时间
//
//        source.registerCorsConfiguration("/**", config);
//        return new CorsFilter(source);
//    }
//}