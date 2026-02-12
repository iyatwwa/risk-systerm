package com.lab.risk.controller;

import com.lab.risk.common.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 健康检查控制器
 */
@Tag(name = "系统管理", description = "系统健康检查和信息查询")
@RestController
@RequestMapping("/api")
public class HealthController {

    @Operation(summary = "健康检查", description = "检查系统是否正常运行")
    @GetMapping("/health")
    public Result<Map<String, Object>> health() {
        Map<String, Object> data = new HashMap<>();
        data.put("status", "UP");
        data.put("application", "lab-risk-system");
        data.put("timestamp", LocalDateTime.now());
        data.put("message", "系统运行正常");
        return Result.success(data);
    }

    @Operation(summary = "系统信息", description = "获取系统版本和环境信息")
    @GetMapping("/info")
    public Result<Map<String, Object>> info() {
        Map<String, Object> data = new HashMap<>();
        data.put("name", "高校实验项目智能风险评估与准入系统");
        data.put("version", "1.0.0");
        data.put("springBootVersion", "3.3.5");
        data.put("javaVersion", System.getProperty("java.version"));
        data.put("osName", System.getProperty("os.name"));
        return Result.success(data);
    }
}