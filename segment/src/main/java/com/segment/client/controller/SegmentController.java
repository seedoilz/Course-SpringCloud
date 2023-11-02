package com.segment.client.controller;

import com.segment.client.service.SegmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

//提供restful服务  供其他服务调用 (我们暂时是consumer-feign调用client)
@RestController
@Slf4j
public class SegmentController {
    @Autowired
    private SegmentService segmentService;


    @CrossOrigin(origins =  "*")
    @GetMapping("/query")
    public String query(@RequestParam(value = "question") String question) throws Exception {
        return segmentService.getAnswer(question);
    }

    @GetMapping("/")
    public String hello() {
        return segmentService.hello();
    }

}