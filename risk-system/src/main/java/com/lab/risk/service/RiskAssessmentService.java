package com.lab.risk.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lab.risk.dto.AssessmentResultDTO;
import com.lab.risk.dto.ProjectAssessmentDTO;
import com.lab.risk.entity.ExperimentProject;
import com.lab.risk.entity.RiskAssessment;
import com.lab.risk.mapper.ExperimentProjectMapper;
import com.lab.risk.mapper.RiskAssessmentMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 风险评估服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RiskAssessmentService extends ServiceImpl<RiskAssessmentMapper, RiskAssessment> {

    private final DroolsRuleService droolsRuleService;
    private final ExperimentProjectMapper projectMapper;

    /**
     * 执行风险评估
     */
    @Transactional(rollbackFor = Exception.class)
    public AssessmentResultDTO assess(ProjectAssessmentDTO dto) {
        log.info("开始风险评估,项目名称: {}", dto.getProjectName());

        // 1. 转换为实体对象
        ExperimentProject project = BeanUtil.copyProperties(dto, ExperimentProject.class);

        // 2. 执行Drools规则评估
        AssessmentResultDTO result = droolsRuleService.executeRules(project);

        // 3. 更新项目风险信息
        if (dto.getProjectId() != null) {
            project.setId(dto.getProjectId());
            project.setRiskScore(result.getRiskScore());
            project.setRiskLevel(result.getRiskLevel().getDesc());
            projectMapper.updateById(project);
        }

        // 4. 保存评估记录
        saveAssessmentRecord(project, result);

        log.info("风险评估完成,分数: {}, 等级: {}", result.getRiskScore(), result.getRiskLevel());

        return result;
    }

    /**
     * 保存评估记录
     */
    private void saveAssessmentRecord(ExperimentProject project, AssessmentResultDTO result) {
        RiskAssessment assessment = new RiskAssessment();
        assessment.setProjectId(project.getId());
        assessment.setProjectCode(project.getProjectCode());
        assessment.setAssessmentType("RULE");
        assessment.setRiskScore(result.getRiskScore());
        assessment.setRiskLevel(result.getRiskLevel());

        // 保存详细信息为JSON
        assessment.setAssessmentDetail(JSONUtil.toJsonStr(result));

        // 保存触发的规则
        String rules = result.getRiskFactors().stream()
                .map(f -> f.getRuleName())
                .collect(Collectors.joining(","));
        assessment.setTriggeredRules(rules);

        // 保存风险项
        String items = result.getRiskFactors().stream()
                .map(f -> f.getFactorName() + "(" + f.getScore() + "分)")
                .collect(Collectors.joining("; "));
        assessment.setRiskItems(items);

        // 保存建议
        assessment.setSuggestions(String.join("; ", result.getSuggestions()));

        assessment.setRequiredApprovalLevel(result.getRequiredApprovalLevel());
        assessment.setAssessedTime(LocalDateTime.now());

        this.save(assessment);
    }

    /**
     * 获取项目的评估历史
     */
    public List<RiskAssessment> getProjectAssessmentHistory(Long projectId) {
        return this.list(new LambdaQueryWrapper<RiskAssessment>()
                .eq(RiskAssessment::getProjectId, projectId)
                .orderByDesc(RiskAssessment::getAssessedTime));
    }
}