package com.lab.risk.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
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
                        .title("实验风险评估系统 API文档")
                        .version("1.0.0")
                        .description("高校实验项目智能风险评估与准入系统 RESTful API 文档")
                        .contact(new Contact()
                                .name("Lab Team")
                                .email("lab@example.com")
                                .url("https://github.com/your-repo"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")));
    }
}