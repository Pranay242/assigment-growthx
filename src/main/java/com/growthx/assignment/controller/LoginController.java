package com.growthx.assignment.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/home")
    public String showLoginPage() {
        return "home";
    }


    @GetMapping("/")
    public String show() {
        return "home";
    }
}
