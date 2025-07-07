package com.pahanaedu.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConfig {

    private final static String url = "jdbc:postgresql://localhost:5432/pahana_edu_31_96";
    private final static String username = "postgres";
    private final static String password = "postgresdb";

    public Connection getConnection() {
        Connection connection;
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Connected");
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return connection;
    }

}
