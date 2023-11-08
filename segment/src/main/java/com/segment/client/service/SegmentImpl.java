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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SegmentImpl implements SegmentService {

    public static String sql = "MATCH (n1:Entity{name:'%s'}) <- [:下属于] - (n2:%s) RETURN n2.name";

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


        return ret.toString();
    }

}
