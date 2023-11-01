package com;
import org.neo4j.driver.*;
import org.neo4j.driver.Record;

import java.util.ArrayList;
import java.util.List;


public class Main {
    public static Driver driver;

    public static String sql = "MATCH (n1:Entity{name:'%s'}) <- [:下属于] - (n2:%s) RETURN n2.name";

    public static void main(String[] args) {
        String uri = "bolt://118.89.203.102:7687";
        String user = "neo4j";
        String password = "Czy026110";

        driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password));

        List<String> answer = getAnswer("pypmml");
        System.out.println(answer.toString());

        driver.close();
    }

    private static List<String> getAnswer(String question){
        List<String> ret = new ArrayList<>();

        try (Session session = driver.session()) {
            Result result = session.run(String.format(sql, question, "Dissdadvantage"));
            while (result.hasNext()) {
                Record record = result.next();
                ret.add(record.get("n2.name").asString());
            }
        }

        return ret;
    }

}
