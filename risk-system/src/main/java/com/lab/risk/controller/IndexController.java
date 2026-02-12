package com.lab.risk.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 首页控制器
 */
@Controller
public class IndexController {

    /**
     * 首页重定向到 API 文档
     */
    @GetMapping("/")
    public String index() {
        return "redirect:/doc.html";
    }

    /**
     * 欢迎页面
     */
    @GetMapping("/welcome")
    public String welcome() {
        return "forward:/index.html";
    }
}