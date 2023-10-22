package com.example.client.controller;

import com.example.client.service.ExampleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

//提供restful服务  供其他服务调用 (我们暂时是consumer-feign调用client)
@RestController
@Slf4j
public class ExampleController {
    @Autowired
    private ExampleService exampleService;

//    @PostMapping("")

//    @GetMapping("")
}
