package com.segment.client.service;

import com.segment.client.nlp.segment.Segment;
import com.segment.client.nlp.segment.impl.MatchSegment;
import com.segment.client.nlp.segment.nature.Nature;
import com.segment.client.nlp.segment.nature.NatureEnum;
import com.google.common.collect.Lists;
import com.segment.client.utils.FileUtils;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.util.*;

@Service
public class SegmentImpl implements SegmentService {

    public static String sql = "MATCH (n1:Entity{name:'%s'}) <- [:下属于] - (n2:%s) RETURN n2.name";

    public static String getEntities = "MATCH (a)-[:下属于]->(t) WHERE t.name <> '' RETURN t.name, rand() as r ORDER BY r LIMIT 25";

    @Autowired
    private Driver driver;

    private static Map<NatureEnum, List<String>> map = new HashMap<>();

    static {
        map.put(NatureEnum.AdvAndDisadv, Lists.newArrayList("Disadvantage", "Advantage"));
        map.put(NatureEnum.Sentiment, Lists.newArrayList("Sentiment"));
        map.put(NatureEnum.Use, Lists.newArrayList("Use"));
    }

    @Override
    public String hello() {
        System.out.println("hello");
        return "hello";
    }

    @Override
    public List<Nature> execSeg(String question) throws FileNotFoundException {
        String vocabPath = SegmentImpl.class.getClassLoader().getResource("vocab").getPath();
        System.out.println(vocabPath);
        Segment segment = new MatchSegment(vocabPath);
        return segment.run(question);
    }

    public String getAnswer(String question) throws FileNotFoundException {
        System.out.println(question);
//        System.out.println(System.getProperty("user.dir"));
        List<Nature> segs = execSeg(question);

        // find entity
        Nature entity = null;
        for (Nature nature : segs) {
            if (nature.getNatureEnum().equals(NatureEnum.Entity)) {
                entity = nature;
                break;
            }
        }

        if (entity == null) {
            return null;
        }

        String entityWord = entity.getWord();
        List<Map<String, List<String>>> ret = new ArrayList<>();
        for (Nature nature : segs) {
            if (nature.getNatureEnum().equals(NatureEnum.Entity)) {
                continue;
            }
            List<String> list = map.get(nature.getNatureEnum());
            if (list == null) {
                continue;
            }
            for (String quesWord : list) {
                Map<String, List<String>> map = new HashMap<>();
                ArrayList<String> ans = new ArrayList<>();
                map.put(quesWord, ans);
                try (Session session = driver.session()) {
                    Result result = session.run(String.format(sql, entityWord, quesWord));
                    while (result.hasNext()) {
                        Record record = result.next();
                        ans.add(record.get("n2.name").asString());
                    }
                }
                ret.add(map);
            }
        }

        Map<String, String> wordMap = new HashMap<>();
        wordMap.put("Disadvantage","劣势");
        wordMap.put("Advantage","优势");
        wordMap.put("Sentiment","情绪");
        wordMap.put("Use","使用场景");
        StringBuilder sb = new StringBuilder();
        for (Map<String, List<String>> listMap : ret) {
            for (String s : listMap.keySet()) {
                if(listMap.get(s).size() == 0){
                    continue;
                }
                sb.append(entityWord).append(wordMap.get(s)).append(": ").append(listMap.get(s).toString());
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    @Override
    public String showEntity(String entityName) {
        if (entityName == null || entityName.length() == 0)
            return null;
        List<Map<String, List<String>>> ret = new ArrayList<>();
        Map<String, List<String>> entityMap = new HashMap<>();
        entityMap.put("Entity", List.of(entityName));
        ret.add(entityMap);
        List<String> quesWords = new ArrayList<>(Arrays.asList("Sentiment", "Use", "Disadvantage", "Advantage"));
        boolean getFound = false;
        for (String quesWord : quesWords) {
            Map<String, List<String>> map = new HashMap<>();
            ArrayList<String> ans = new ArrayList<>();
            map.put(quesWord, ans);
            try (Session session = driver.session()) {
                Result result = session.run(String.format(sql, entityName,  quesWord));
                while (result.hasNext()) {
                    Record record = result.next();
                    ans.add(record.get("n2.name").asString());
                    getFound = true;
                }
            }
            ret.add(map);
        }
        if (getFound)
            return ret.toString();
        else
            return null;
    }

    @Override
    public String randomEntities() {
        ArrayList<String> entityNames = new ArrayList<>();
        try (Session session = driver.session()) {
            Result result = session.run(getEntities);
            while (result.hasNext()) {
                Record record = result.next();
                entityNames.add(record.get("t.name").asString());
            }
        }
        ArrayList<String> ans = new ArrayList<>();
        for (String entityName : entityNames) {
            ans.add(showEntity(entityName));
        }
        return ans.toString();
    }

}
