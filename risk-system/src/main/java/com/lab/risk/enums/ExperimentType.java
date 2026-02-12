package com.lab.risk.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 实验类型枚举
 */
@Getter
public enum ExperimentType {
    CHEMISTRY(1, "化学实验"),
    BIOLOGY(2, "生物实验"),
    PHYSICS(3, "物理实验"),
    ELECTRICAL(4, "电气实验"),
    MECHANICAL(5, "机械实验"),
    RADIATION(6, "辐射实验"),
    LASER(7, "激光实验"),
    HIGH_PRESSURE(8, "高压实验"),
    HIGH_TEMP(9, "高温实验"),
    OTHER(99, "其他实验");

    @EnumValue
    private final Integer code;

    @JsonValue
    private final String desc;

    ExperimentType(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}