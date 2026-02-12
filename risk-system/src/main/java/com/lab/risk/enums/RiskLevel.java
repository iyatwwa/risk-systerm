package com.lab.risk.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 风险等级枚举
 */
@Getter
public enum RiskLevel {
    LOW(1, "低风险", "green", 0, 40),
    MEDIUM(2, "中风险", "yellow", 41, 70),
    HIGH(3, "高风险", "orange", 71, 90),
    CRITICAL(4, "极高风险", "red", 91, 100);

    @EnumValue
    private final Integer code;

    @JsonValue
    private final String desc;

    private final String color;
    private final Integer minScore;
    private final Integer maxScore;

    RiskLevel(Integer code, String desc, String color, Integer minScore, Integer maxScore) {
        this.code = code;
        this.desc = desc;
        this.color = color;
        this.minScore = minScore;
        this.maxScore = maxScore;
    }

    /**
     * 根据分数获取风险等级
     */
    public static RiskLevel getByScore(Integer score) {
        if (score == null) return LOW;
        for (RiskLevel level : values()) {
            if (score >= level.minScore && score <= level.maxScore) {
                return level;
            }
        }
        return LOW;
    }
}