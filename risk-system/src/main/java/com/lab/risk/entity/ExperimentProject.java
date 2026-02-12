package com.lab.risk.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.lab.risk.enums.DangerLevel;
import com.lab.risk.enums.ExperimentType;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 实验项目实体
 */
@Data
@TableName("t_experiment_project")
public class ExperimentProject {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 项目编号 */
    private String projectCode;

    /** 项目名称 */
    private String projectName;

    /** 实验类型 */
    private ExperimentType experimentType;

    /** 危险等级 */
    private DangerLevel dangerLevel;

    /** 申请人ID */
    private Long applicantId;

    /** 申请人姓名 */
    private String applicantName;

    /** 实验地点 */
    private String location;

    /** 参与人数 */
    private Integer participantCount;

    /** 实验时长(小时) */
    private Integer duration;

    /** 是否涉及危险化学品 */
    private Boolean hasDangerousChemicals;

    /** 危险化学品名称 */
    private String chemicalNames;

    /** 是否涉及生物安全 */
    private Boolean hasBioSafety;

    /** 生物安全等级 */
    private Integer bioSafetyLevel;

    /** 是否涉及辐射 */
    private Boolean hasRadiation;

    /** 辐射剂量 */
    private String radiationDose;

    /** 操作人员是否具备资质 */
    private Boolean hasQualification;

    /** 是否有应急预案 */
    private Boolean hasEmergencyPlan;

    /** 项目描述 */
    private String description;

    /** 风险评估分数 */
    private Integer riskScore;

    /** 风险等级 */
    private String riskLevel;

    /** 状态 */
    private String status;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /** 更新时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /** 逻辑删除 */
    @TableLogic
    private Integer deleted;
}