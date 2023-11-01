package com.example.client.utils;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;

@Component
public class Neo4jDriverFactory implements FactoryBean<Driver> {

    private Driver driver = null;
    private String uri = "bolt://118.89.203.102:7687";
    private String user = "neo4j";
    private String password = "Czy026110";

    @Override
    public Driver getObject() throws Exception {
        if(driver == null){
            driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password));
        }
        return driver;
    }

    @Override
    public Class<?> getObjectType() {
        return Driver.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
