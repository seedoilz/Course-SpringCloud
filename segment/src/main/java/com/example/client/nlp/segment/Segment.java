package com.example.client.nlp.segment;

import com.example.client.nlp.segment.nature.Nature;

import java.util.List;

public interface Segment {
    /**
     * 分词器
     */

    List<Nature> run(String text);

}
