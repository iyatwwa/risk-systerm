package com.lab.risk.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 危险等级枚举
 */
@Getter
public enum DangerLevel {
    LEVEL_0(0, "无危险"),
    LEVEL_1(1, "轻微危险"),
    LEVEL_2(2, "一般危险"),
    LEVEL_3(3, "较大危险"),
    LEVEL_4(4, "重大危险"),
    LEVEL_5(5, "特别重大危险");

    @EnumValue
    private final Integer code;

    @JsonValue
    private final String desc;

    DangerLevel(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}