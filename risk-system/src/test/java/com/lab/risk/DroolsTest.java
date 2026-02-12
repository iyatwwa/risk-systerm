package com.lab.risk;

import com.lab.risk.dto.AssessmentResultDTO;
import com.lab.risk.entity.ExperimentProject;
import com.lab.risk.enums.DangerLevel;
import com.lab.risk.enums.ExperimentType;
import com.lab.risk.service.DroolsRuleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Drools规则引擎测试
 */
@SpringBootTest
public class DroolsTest {

    @Autowired
    private DroolsRuleService droolsRuleService;

    @Test
    public void testHighRiskChemistry() {
        // 测试场景1: 高风险化学实验
        ExperimentProject project = new ExperimentProject();
        project.setProjectName("浓硫酸氧化实验");
        project.setExperimentType(ExperimentType.CHEMISTRY);
        project.setDangerLevel(DangerLevel.LEVEL_4);
        project.setHasDangerousChemicals(true);
        project.setChemicalNames("浓硫酸");
        project.setHasQualification(false);
        project.setHasEmergencyPlan(false);
        project.setParticipantCount(15);
        project.setDuration(3);
        project.setLocation("化学实验室A101");

        AssessmentResultDTO result = droolsRuleService.executeRules(project);

        System.out.println("========== 评估结果 ==========");
        System.out.println("风险分数: " + result.getRiskScore());
        System.out.println("风险等级: " + result.getRiskLevel().getDesc());
        System.out.println("审批层级: " + result.getRequiredApprovalLevel());
        System.out.println("是否自动通过: " + result.getAutoApprove());

        System.out.println("\n========== 风险因素 ==========");
        result.getRiskFactors().forEach(factor -> {
            System.out.println(factor.getFactorName() + ": " + factor.getScore() + "分 - " + factor.getDescription());
        });

        System.out.println("\n========== 建议措施 ==========");
        result.getSuggestions().forEach(System.out::println);
    }

    @Test
    public void testLowRiskPhysics() {
        // 测试场景2: 低风险物理实验
        ExperimentProject project = new ExperimentProject();
        project.setProjectName("简单电路实验");
        project.setExperimentType(ExperimentType.PHYSICS);
        project.setDangerLevel(DangerLevel.LEVEL_1);
        project.setHasDangerousChemicals(false);
        project.setHasQualification(true);
        project.setHasEmergencyPlan(true);
        project.setParticipantCount(10);
        project.setDuration(2);
        project.setLocation("物理实验室B201");

        AssessmentResultDTO result = droolsRuleService.executeRules(project);

        System.out.println("风险分数: " + result.getRiskScore());
        System.out.println("风险等级: " + result.getRiskLevel().getDesc());
        System.out.println("是否自动通过: " + result.getAutoApprove());
    }
}
