package com.example.client.controller;

import com.example.client.nlp.segment.nature.Nature;
import com.example.client.service.ExampleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//提供restful服务  供其他服务调用 (我们暂时是consumer-feign调用client)
@RestController
@Slf4j
public class ExampleController {
    @Autowired
    private ExampleService exampleService;


    @CrossOrigin(origins =  "*")
    @GetMapping("/query")
    public String query(@RequestParam(value = "question") String question) throws Exception {
        return exampleService.getAnswer(question);
    }

    @GetMapping("/")
    public String hello() {
        return exampleService.hello();
    }

}
