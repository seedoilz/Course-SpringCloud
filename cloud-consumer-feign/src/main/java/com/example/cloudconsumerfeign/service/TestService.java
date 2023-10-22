package com.example.cloudconsumerfeign.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
/**
 * name 指定调用rest接口所对应的服务名
 * path指定调用rest接口所在的StockController指定的@RequestMapping
 */
//@FeignClient(name = "示例知识图谱展示服务", path= "")
@FeignClient(name = "test-service")
public interface TestService {
    //声明需要调用的rest接口对应的方法
    @RequestMapping("")
    String something();
}
