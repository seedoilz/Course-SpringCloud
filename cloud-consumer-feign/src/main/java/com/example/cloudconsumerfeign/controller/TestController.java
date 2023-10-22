package com.example.cloudconsumerfeign.controller;

import com.example.cloudconsumerfeign.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("")
public class TestController {
//    @Autowired
    TestService testService;
//    @RequestMapping("/test")
//    @GetMapping
}
