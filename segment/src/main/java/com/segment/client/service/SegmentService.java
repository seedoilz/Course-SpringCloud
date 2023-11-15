package com.segment.client.service;

import com.segment.client.nlp.segment.nature.Nature;

import java.io.FileNotFoundException;
import java.util.List;

public interface SegmentService {
    String hello();

    List<Nature> execSeg(String question) throws FileNotFoundException;

    String getAnswer(String question) throws FileNotFoundException;

    String showEntity(String entityName);

    String randomEntities();
}
