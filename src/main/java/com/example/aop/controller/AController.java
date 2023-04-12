package com.example.aop.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/scopeA")
public class AController {

    @GetMapping("/func1")
    public String A1(String arg) {
        String msg = "scopeA - func1 执行";
        System.out.println(msg);
        return msg;
    }

    @GetMapping("/func2")
    public String A2() {
        String msg = "scopeA - func1 执行";
        System.out.println(msg);
        return msg;
    }

}
