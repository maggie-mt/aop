package com.example.aop.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/scopeB")
public class BController {

    @GetMapping("/func1")
    public String B1() {
        String msg = "scopeB - func2 执行";
        System.out.println(msg);
        return msg;
    }

}
