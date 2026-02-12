package com.lab.risk.controller;

import com.lab.risk.common.Result;
import com.lab.risk.dto.AssessmentResultDTO;
import com.lab.risk.dto.ProjectAssessmentDTO;
import com.lab.risk.entity.RiskAssessment;
import com.lab.risk.service.RiskAssessmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 风险评估控制器
 */
@Tag(name = "风险评估管理")
@RestController
@RequestMapping("/api/risk-assessment")
@RequiredArgsConstructor
public class RiskAssessmentController {

    private final RiskAssessmentService assessmentService;

    @Operation(summary = "执行风险评估")
    @PostMapping("/assess")
    public Result<AssessmentResultDTO> assess(@Valid @RequestBody ProjectAssessmentDTO dto) {
        AssessmentResultDTO result = assessmentService.assess(dto);
        return Result.success(result);
    }

    @Operation(summary = "获取项目评估历史")
    @GetMapping("/history/{projectId}")
    public Result<List<RiskAssessment>> getHistory(@PathVariable Long projectId) {
        List<RiskAssessment> history = assessmentService.getProjectAssessmentHistory(projectId);
        return Result.success(history);
    }
}