package com.lab.risk.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * Spring Security 安全配置
 * 开发阶段完全禁用认证
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 禁用 CSRF
                .csrf(AbstractHttpConfigurer::disable)

                // 配置 CORS
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // 允许所有请求
                .authorizeHttpRequests(auth -> auth
                        // Knife4j 和 Swagger 相关路径 - 必须完全放行
                        .requestMatchers(
                                "/doc.html",
                                "/doc.html/**",
                                "/v3/api-docs",
                                "/v3/api-docs/**",
                                "/swagger-resources",
                                "/swagger-resources/**",
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/webjars/**",
                                "/favicon.ico",
                                "/error"
                        ).permitAll()
                        // Druid 监控
                        .requestMatchers("/druid/**").permitAll()
                        // 静态资源
                        .requestMatchers("/", "/index.html", "/static/**").permitAll()
                        // API 接口
                        .requestMatchers("/api/**").permitAll()
                        // 其他所有请求
                        .anyRequest().permitAll()
                )

                // 禁用表单登录
                .formLogin(AbstractHttpConfigurer::disable)

                // 禁用 HTTP Basic
                .httpBasic(AbstractHttpConfigurer::disable)

                // 禁用登出
                .logout(AbstractHttpConfigurer::disable)

                // 禁用 Session
                .sessionManagement(AbstractHttpConfigurer::disable);

        return http.build();
    }

    /**
     * CORS 配置源
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}