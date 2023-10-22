package com.example.client.service;

import com.example.client.dao.ExampleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExampleImpl implements ExampleService {
//    @Autowired(同一项目实体类无需自动导入)
    ExampleDao exampleDao;
    @Override
    public String hello(){
        System.out.println("hello");
        return "hello";
    }
}
