package com.pahanaedu.config.db.factory;

import java.sql.Connection;

public interface DbConnectionFactory {
    Connection getConnection(String db) throws ClassNotFoundException;
}
