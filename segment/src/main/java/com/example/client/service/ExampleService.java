package com.example.client.service;

import com.example.client.nlp.segment.nature.Nature;

import java.io.FileNotFoundException;
import java.util.List;

public interface ExampleService {
    String hello();

    List<Nature> execSeg(String question) throws FileNotFoundException;

    String getAnswer(String question) throws FileNotFoundException;
}
