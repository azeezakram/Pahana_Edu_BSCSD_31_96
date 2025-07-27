package com.pahanaedu.config.db.impl;

import com.pahanaedu.config.db.factory.DbConnection;

public class TestDb implements DbConnection {

    private static final DbConnection DB = new TestDb();
    private final static String url = "jdbc:postgresql://localhost:5432/pahana_edu_test_31_96";
    private final static String username = "postgres";
    private final static String password = "postgresdb";


    public static DbConnection getInstance() {
        return DB;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }
}
