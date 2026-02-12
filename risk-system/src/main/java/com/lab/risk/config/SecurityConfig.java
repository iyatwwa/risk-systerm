package com.lab.risk.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

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

                // 禁用 CORS
                .cors(AbstractHttpConfigurer::disable)

                // 允许所有请求
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                )

                // 禁用表单登录
                .formLogin(AbstractHttpConfigurer::disable)

                // 禁用 HTTP Basic
                .httpBasic(AbstractHttpConfigurer::disable)

                // 禁用登出
                .logout(AbstractHttpConfigurer::disable)

                // 禁用匿名用户
                .anonymous(AbstractHttpConfigurer::disable);

        return http.build();
    }
}