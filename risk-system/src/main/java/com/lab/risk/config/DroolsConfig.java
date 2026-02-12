package com.lab.risk.config;

import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieModule;
import org.kie.api.runtime.KieContainer;
import org.kie.internal.io.ResourceFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Drools规则引擎配置
 */
@Configuration
public class DroolsConfig {

    private static final String RULES_PATH = "rules/";

    /**
     * 创建KieFileSystem
     */
    @Bean
    public KieFileSystem kieFileSystem() {
        KieFileSystem kieFileSystem = KieServices.Factory.get().newKieFileSystem();
        // 加载规则文件
        kieFileSystem.write(ResourceFactory.newClassPathResource(RULES_PATH + "risk-assessment-rules.drl"));
        return kieFileSystem;
    }

    /**
     * 创建KieContainer
     */
    @Bean
    public KieContainer kieContainer() {
        KieServices kieServices = KieServices.Factory.get();
        KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem());
        kieBuilder.buildAll();

        KieModule kieModule = kieBuilder.getKieModule();
        return kieServices.newKieContainer(kieModule.getReleaseId());
    }
}