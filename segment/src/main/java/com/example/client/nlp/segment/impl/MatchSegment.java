package com.example.client.nlp.segment.impl;

import com.example.client.nlp.segment.Segment;
import com.example.client.nlp.segment.nature.Nature;
import com.example.client.nlp.segment.nature.NatureEnum;
import com.example.client.utils.FileUtils;

import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class MatchSegment implements Segment {

    /**
     * 词表
     */
    private List<String> words;

    /**
     * 词与词性映射
     */
    private Map<String, NatureEnum> wordToNature;

    /**
     * 最大切词长度
     */
    private int maxLen;

    public MatchSegment(String vocabPath) {
        List<String> paths = FileUtils.getFiles(vocabPath);
        words = new ArrayList<>();
        wordToNature = new HashMap<>();
        for (String path : paths) {
            String name = FileUtils.getFileName(path);
            NatureEnum natureEnum = NatureEnum.parser(name);
            List<String> localWords = FileUtils.readLines(path);
            for(String word: localWords){
                words.add(word);
                wordToNature.put(word, natureEnum);
            }
        }
        OptionalInt optionalInt = words.stream().mapToInt(String::length).reduce(Integer::max);
        if (optionalInt.isPresent()) {
            this.maxLen = optionalInt.getAsInt();
        } else {
            this.maxLen = 0;
        }
    }

    /**
     * 后向最大匹配分词
     *
     * @paran text
     */
    private List<String> segmentBackwardLongest(String text) {
        List<String> wordList = new ArrayList<>();
        int localMaxLen = text.length() > this.maxLen ? this.maxLen : text.length();
        int end = text.length();
        while (end > 0) {
            int start = end - localMaxLen > 0 ? end - localMaxLen : 0;
            String subString = text.substring(start, end);
            while (true) {
                if (subString.length() == 1) {
                    wordList.add(subString);
                    break;
                } else if (words.contains(subString)) {
                    wordList.add(subString);
                    break;
                } else {
                    subString = subString.substring(1);
                }
            }
            end = end - subString.length();
        }
        // 数组反转
        Collections.reverse(wordList);
        return wordList;
    }

    /**
     * 前向最大匹配分词
     *
     * @param text
     */
    private List<String> segmentForwardLongest(String text) {
        List<String> wordList = new ArrayList<>();
        System.out.println(words);
        // 最大长度不允许超过句子长度
        int localMaxLen = text.length() > this.maxLen ? this.maxLen : text.length();
        int start = 0;
        while (start < text.length()) {
            // 结束位置
            int end = start + localMaxLen > text.length() ? text.length() : start + localMaxLen;
            String subString = text.substring(start, end);
            while (true) {
                if (subString.length() == 1) {
                    wordList.add(subString);
                    break;
                }
                if (words.contains(subString)) {
                    wordList.add(subString);
                    break;
                } else {
                    subString = subString.substring(0, subString.length() - 1);
                }
            }
            start += subString.length();
        }
        return wordList;
    }

    /**
     * 双向最大匹配分词
     *
     * @param text
     */
    private List<String> segment(String text) {
        List<String> forwardWords = this.segmentForwardLongest(text);
        List<String> backwardWords = this.segmentBackwardLongest(text);

        if (forwardWords.size() != backwardWords.size()) {
            return forwardWords.size() > backwardWords.size() ? forwardWords : backwardWords;
        }
        return forwardWords.stream().distinct().count() > backwardWords.stream().distinct().count() ?
                forwardWords : backwardWords;
    }

    @Override
    public List<Nature> run(String text) {
        List<String> words = this.segment(text);
        return words
                .stream()
                .map(word -> Nature.create(word, this.wordToNature.getOrDefault(word, NatureEnum.Other)))
                .collect(Collectors.toList());
    }

    public static void main(String[] args) throws FileNotFoundException {
        String str = "评价数据挖掘的使用场景与优点";
        Segment segment = new MatchSegment(FileUtils.getSpringPath("vocab"));
        List<Nature> result = segment.run(str);
        System.out.println(Arrays.toString(result.toArray()));
    }
}
