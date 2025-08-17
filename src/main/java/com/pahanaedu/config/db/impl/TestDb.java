package com.pahanaedu.config.db.impl;

import com.pahanaedu.config.db.factory.DbConnection;

public class TestDb implements DbConnection {

    private static final DbConnection INSTANCE = new TestDb();
    private static final String URL = "jdbc:h2:mem:testdb;MODE=PostgreSQL;DB_CLOSE_DELAY=-1";
    private static final String USERNAME = "sa";
    private static final String PASSWORD = "";


    public static DbConnection getInstance() throws ClassNotFoundException {
        Class.forName("org.h2.Driver");
        return INSTANCE;
    }

    @Override
    public String getUrl() {
        return URL;
    }

    @Override
    public String getUsername() {
        return USERNAME;
    }

    @Override
    public String getPassword() {
        return PASSWORD;
    }
}
