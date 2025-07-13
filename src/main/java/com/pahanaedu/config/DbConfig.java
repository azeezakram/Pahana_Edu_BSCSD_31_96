package com.pahanaedu.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConfig {

    private final static String url = "jdbc:postgresql://localhost:5432/pahana_edu_31_96";
    private final static String username = "postgres";
    private final static String password = "postgresdb";


    public DbConfig () {}

    public Connection getConnection() {

        try {
            Class.forName("org.postgresql.Driver");
            return DriverManager.getConnection(url, username, password);

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
