package com.pahanaedu.config.db.init;

import com.pahanaedu.config.db.impl.TestDb;
import org.h2.tools.Server;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Scanner;

public class TestDbInitializer {

    private static boolean initialized = false;

    public static void initializeTestDb() {
        if (initialized) return;

        try {
            Server server = Server.createWebServer("-web", "-webPort", "9090").start();

            try (Connection conn = DriverManager.getConnection(
                    TestDb.getInstance().getUrl(),
                    TestDb.getInstance().getUsername(),
                    TestDb.getInstance().getPassword());
                 Statement stmt = conn.createStatement()) {

                try (InputStream is = TestDbInitializer.class.getClassLoader().getResourceAsStream("testdb.sql")) {
                    if (is == null) throw new RuntimeException("testdb.sql not found in resources");

                    String schemaSQL = new Scanner(is, StandardCharsets.UTF_8)
                            .useDelimiter("\\A")
                            .next();

                    for (String sql : schemaSQL.split(";")) {
                        sql = sql.trim();
                        if (!sql.isEmpty()) {
                            stmt.execute(sql);
                        }
                    }
                }
            }

            initialized = true;
            System.out.println("H2 Test DB initialized successfully!");

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize H2 Test DB", e);
        }
    }
}
