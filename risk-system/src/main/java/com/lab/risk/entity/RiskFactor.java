package com.lab.risk.entity;

import lombok.Data;

/**
 * 风险因素 (用于Drools规则匹配)
 */
@Data
public class RiskFactor {

    /** 因素名称 */
    private String factorName;

    /** 风险分数 */
    private Integer score;

    /** 风险描述 */
    private String description;

    /** 触发规则 */
    private String ruleName;

    public RiskFactor() {}

    public RiskFactor(String factorName, Integer score, String description, String ruleName) {
        this.factorName = factorName;
        this.score = score;
        this.description = description;
        this.ruleName = ruleName;
    }
}