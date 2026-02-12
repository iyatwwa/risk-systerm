package com.lab.risk.dto;

import com.lab.risk.entity.RiskFactor;
import com.lab.risk.enums.RiskLevel;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 评估结果DTO
 */
@Data
public class AssessmentResultDTO {

    /** 风险分数 */
    private Integer riskScore = 0;

    /** 风险等级 */
    private RiskLevel riskLevel;

    /** 风险因素列表 */
    private List<RiskFactor> riskFactors = new ArrayList<>();

    /** 建议措施 */
    private List<String> suggestions = new ArrayList<>();

    /** 所需审批层级 */
    private Integer requiredApprovalLevel = 1;

    /** 评估摘要 */
    private String summary;

    /** 是否可以自动通过 */
    private Boolean autoApprove = false;

    /**
     * 添加风险因素
     */
    public void addRiskFactor(RiskFactor factor) {
        this.riskFactors.add(factor);
        this.riskScore += factor.getScore();
    }

    /**
     * 添加建议
     */
    public void addSuggestion(String suggestion) {
        this.suggestions.add(suggestion);
    }

    /**
     * 计算风险等级
     */
    public void calculateRiskLevel() {
        this.riskLevel = RiskLevel.getByScore(this.riskScore);

        // 根据风险等级确定审批层级
        switch (this.riskLevel) {
            case LOW -> this.requiredApprovalLevel = 1;
            case MEDIUM -> this.requiredApprovalLevel = 2;
            case HIGH -> this.requiredApprovalLevel = 3;
            case CRITICAL -> this.requiredApprovalLevel = 4;
        }

        // 低风险可自动通过
        this.autoApprove = this.riskLevel == RiskLevel.LOW;
    }
}