package com.pahanaedu.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnectionFactory {

    public static Connection getConnection() throws SQLException {

        DbConfig config = DbConfig.getInstance();

        try {
            Class.forName("org.postgresql.Driver");
            return DriverManager.getConnection(
                    config.getUrl(),
                    config.getUsername(),
                    config.getPassword()
            );
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
}

