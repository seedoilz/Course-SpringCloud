package com.example.client.service;

import com.example.client.dao.ExampleDao;
import com.example.client.nlp.segment.Segment;
import com.example.client.nlp.segment.impl.MatchSegment;
import com.example.client.nlp.segment.nature.Nature;
import com.example.client.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.util.List;

@Service
public class ExampleImpl implements ExampleService {
//    @Autowired(同一项目实体类无需自动导入)
    ExampleDao exampleDao;
    @Override
    public String hello(){
        System.out.println("hello");
        return "hello";
    }

    @Override
    public List<Nature> execSeg(String question) throws FileNotFoundException {
        Segment segment = new MatchSegment(FileUtils.getSpringPath("vocab"));
        return segment.run(question);
    }
}
