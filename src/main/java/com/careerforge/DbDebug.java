package com.careerforge;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class DbDebug {

    @Autowired
    private DataSource dataSource;

    @PostConstruct
    public void printDb() throws Exception {
        System.out.println("✅ REAL DB URL: " +
                dataSource.getConnection().getMetaData().getURL());
    }
}