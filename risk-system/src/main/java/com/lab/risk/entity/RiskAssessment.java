package com.lab.risk.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.lab.risk.enums.RiskLevel;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 风险评估记录实体
 */
@Data
@TableName("t_risk_assessment")
public class RiskAssessment {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 项目ID */
    private Long projectId;

    /** 项目编号 */
    private String projectCode;

    /** 评估类型 (RULE-规则引擎, ML-机器学习, MANUAL-人工) */
    private String assessmentType;

    /** 风险分数 */
    private Integer riskScore;

    /** 风险等级 */
    private RiskLevel riskLevel;

    /** 评估详情(JSON格式) */
    private String assessmentDetail;

    /** 触发的规则列表 */
    private String triggeredRules;

    /** 风险项列表 */
    private String riskItems;

    /** 建议措施 */
    private String suggestions;

    /** 所需审批层级 */
    private Integer requiredApprovalLevel;

    /** 评估人ID */
    private Long assessorId;

    /** 评估时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime assessedTime;

    /** 备注 */
    private String remark;
}