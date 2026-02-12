package com.lab.risk.service;

import com.lab.risk.dto.AssessmentResultDTO;
import com.lab.risk.entity.ExperimentProject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.stereotype.Service;

/**
 * Drools规则引擎服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DroolsRuleService {

    private final KieContainer kieContainer;

    /**
     * 执行风险评估规则
     */
    public AssessmentResultDTO executeRules(ExperimentProject project) {
        log.info("开始执行Drools规则评估,项目: {}", project.getProjectName());

        // 创建会话
        KieSession kieSession = kieContainer.newKieSession();

        try {
            // 创建结果对象
            AssessmentResultDTO result = new AssessmentResultDTO();

            // 设置全局变量
            kieSession.setGlobal("result", result);

            // 插入事实对象
            kieSession.insert(project);

            // 触发规则
            int ruleCount = kieSession.fireAllRules();
            log.info("触发了 {} 条规则", ruleCount);

            // 计算风险等级
            result.calculateRiskLevel();

            // 生成评估摘要
            result.setSummary(generateSummary(result));

            log.info("规则评估完成, 风险分数: {}, 风险等级: {}",
                    result.getRiskScore(), result.getRiskLevel());

            return result;

        } finally {
            kieSession.dispose();
        }
    }

    /**
     * 生成评估摘要
     */
    private String generateSummary(AssessmentResultDTO result) {
        return String.format("经系统评估,该项目风险等级为【%s】,风险分数为 %d 分,共识别出 %d 个风险因素,需 %d 级审批。",
                result.getRiskLevel().getDesc(),
                result.getRiskScore(),
                result.getRiskFactors().size(),
                result.getRequiredApprovalLevel());
    }
}
