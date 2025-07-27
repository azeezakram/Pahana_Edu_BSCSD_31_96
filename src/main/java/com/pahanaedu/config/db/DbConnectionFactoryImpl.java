package com.pahanaedu.config.db;

import com.pahanaedu.config.db.exception.DatabaseConnectionException;
import com.pahanaedu.config.db.factory.DbConnection;
import com.pahanaedu.config.db.factory.DbConnectionFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnectionFactoryImpl implements DbConnectionFactory {
    @Override
    public Connection getConnection(String dbType) throws ClassNotFoundException {

        if (dbType == null || dbType.isEmpty()) {
            throw new DatabaseConnectionException("Provide database type!");
        }

        Class.forName("org.postgresql.Driver");

        try {
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
}

