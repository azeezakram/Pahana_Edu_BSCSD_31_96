package com.pahanaedu.config.db.impl;

import com.pahanaedu.config.db.exception.DatabaseConnectionException;
import com.pahanaedu.config.db.factory.DbConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnectionFactory implements com.pahanaedu.config.db.factory.DbConnectionFactory {
    @Override
    public Connection getConnection(String dbType) throws ClassNotFoundException {

        if (dbType == null || dbType.isEmpty()) {
            throw new DatabaseConnectionException("Provide database type!");
        }

        Class.forName("org.postgresql.Driver");

        try {
            DbConnection dbConnection = getDbConnection(dbType);

            return DriverManager.getConnection(
                    dbConnection.getUrl(),
                    dbConnection.getUsername(),
                    dbConnection.getPassword()
            );


        } catch (SQLException e) {
            throw new DatabaseConnectionException("Database connection failed!");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static DbConnection getDbConnection(String dbType) {
        DbConnection dbConnection;
        switch (dbType.toLowerCase()) {
            case "production": {
                dbConnection = ProductionDb.getInstance();
                break;
            }
            case "test": {
                dbConnection = TestDb.getInstance();
                break;
            }
            default: {
                throw new DatabaseConnectionException("Database not found!");
            }
        }

        assert dbConnection != null;
        return dbConnection;
    }
}

