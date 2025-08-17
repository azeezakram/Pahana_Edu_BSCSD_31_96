package com.pahanaedu.config.db.impl;

import com.pahanaedu.config.db.factory.DbConnection;

public class ProductionDb implements DbConnection {
    private static final DbConnection INSTANCE = new ProductionDb();
    private final static String URL = "jdbc:postgresql://localhost:5432/pahana_edu_31_96";
    private final static String USERNAME = "postgres";
    private final static String PASSWORD = "postgresdb";

    private ProductionDb() {
    }

    public static DbConnection getInstance() throws ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
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
