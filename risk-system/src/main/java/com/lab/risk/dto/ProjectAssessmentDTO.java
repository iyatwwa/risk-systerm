package com.lab.risk.dto;

import com.lab.risk.enums.DangerLevel;
import com.lab.risk.enums.ExperimentType;
import lombok.Data;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 项目评估请求DTO
 */
@Data
public class ProjectAssessmentDTO {

    /** 项目ID (更新评估时需要) */
    private Long projectId;

    @NotBlank(message = "项目名称不能为空")
    private String projectName;

    @NotNull(message = "实验类型不能为空")
    private ExperimentType experimentType;

    @NotNull(message = "危险等级不能为空")
    private DangerLevel dangerLevel;

    @NotBlank(message = "实验地点不能为空")
    private String location;

    @NotNull(message = "参与人数不能为空")
    @Min(value = 1, message = "参与人数至少为1人")
    private Integer participantCount;

    @NotNull(message = "实验时长不能为空")
    @Min(value = 1, message = "实验时长至少为1小时")
    private Integer duration;

    /** 是否涉及危险化学品 */
    private Boolean hasDangerousChemicals = false;

    /** 危险化学品名称 */
    private String chemicalNames;

    /** 是否涉及生物安全 */
    private Boolean hasBioSafety = false;

    /** 生物安全等级 1-4 */
    private Integer bioSafetyLevel;

    /** 是否涉及辐射 */
    private Boolean hasRadiation = false;

    /** 辐射剂量 */
    private String radiationDose;

    /** 操作人员是否具备资质 */
    private Boolean hasQualification = true;

    /** 是否有应急预案 */
    private Boolean hasEmergencyPlan = true;

    /** 项目描述 */
    private String description;
}