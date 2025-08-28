package com.nghia.ecommerce.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.cors.*;

import java.util.Arrays;

@Configuration
public class CorsConfig {

    @Bean
    @Primary // <- Đánh dấu bean này là ưu tiên
    public CorsConfigurationSource corsConfigurationSource(
            @Value("${cors.allowed-origins:*}") String origins,
            @Value("${cors.allowed-methods:GET,POST,PUT,DELETE,OPTIONS,PATCH}") String methods,
            @Value("${cors.allowed-headers:*}") String headers,
            @Value("${cors.allow-credentials:true}") boolean allowCredentials
    ) {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.asList(origins.split(",")));
        config.setAllowedMethods(Arrays.asList(methods.split(",")));
        config.setAllowedHeaders(Arrays.asList(headers.split(",")));
        config.setAllowCredentials(allowCredentials);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
