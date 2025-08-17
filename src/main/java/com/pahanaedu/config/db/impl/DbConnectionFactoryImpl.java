package com.pahanaedu.config.db.impl;

import com.pahanaedu.config.db.exception.DatabaseConnectionException;
import com.pahanaedu.config.db.factory.DbConnection;
import com.pahanaedu.config.db.factory.DbConnectionFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnectionFactoryImpl implements DbConnectionFactory {

    @Override
    public Connection getConnection(String dbType) {
        if (dbType == null || dbType.isEmpty()) {
            throw new DatabaseConnectionException("Provide database type!");
        }

        try {
            DbConnection dbConnection = getDbConnection(dbType);
            return DriverManager.getConnection(
                    dbConnection.getUrl(),
                    dbConnection.getUsername(),
                    dbConnection.getPassword()
            );
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to connect to database: " + e.getMessage(), e);
        }
    }

    private static DbConnection getDbConnection(String dbType) throws ClassNotFoundException {
        return switch (dbType.toLowerCase()) {
            case "production" -> ProductionDb.getInstance();
            case "test" -> TestDb.getInstance();
            default -> throw new DatabaseConnectionException("Database type not found: " + dbType);
        };
    }
}
