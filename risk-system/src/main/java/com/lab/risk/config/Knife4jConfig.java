package com.lab.risk.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.ExternalDocumentation;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Knife4j/OpenAPI 配置
 */
@Configuration
public class Knife4jConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("实验风险评估系统 API")
                        .version("1.0.0")
                        .description("高校实验项目智能风险评估与准入系统 RESTful API 文档")
                        .contact(new Contact()
                                .name("Lab Team")
                                .email("lab@example.com")
                                .url("https://github.com/iyatwwa/risk-systerm"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")))
                .externalDocs(new ExternalDocumentation()
                        .description("项目文档")
                        .url("https://github.com/iyatwwa/risk-systerm"));
    }

    /**
     * 全部接口分组 (使用英文名称避免编码问题)
     */
    @Bean
    public GroupedOpenApi allApi() {
        return GroupedOpenApi.builder()
                .group("all-api")  // 使用英文名称
                .displayName("全部接口")  // 显示名称可以用中文
                .pathsToMatch("/api/**")
                .build();
    }

    /**
     * 风险评估模块
     */
    @Bean
    public GroupedOpenApi riskAssessmentApi() {
        return GroupedOpenApi.builder()
                .group("risk-assessment")
                .displayName("风险评估模块")
                .pathsToMatch("/api/risk-assessment/**")
                .build();
    }

    /**
     * 系统管理模块
     */
    @Bean
    public GroupedOpenApi systemApi() {
        return GroupedOpenApi.builder()
                .group("system")
                .displayName("系统管理")
                .pathsToMatch("/api/health", "/api/info")
                .build();
    }
}