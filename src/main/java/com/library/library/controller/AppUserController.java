package com.library.library.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppUserController {
    @GetMapping("/test")
    public String test() {
        return "test";
    }
}
