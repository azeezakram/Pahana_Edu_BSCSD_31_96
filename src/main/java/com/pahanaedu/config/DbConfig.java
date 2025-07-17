package com.pahanaedu.config;

public class DbConfig {

    private final static DbConfig dbConfig = new DbConfig();
    private final String url;
    private final String username;
    private final String password;


    public DbConfig () {

        this.url = "jdbc:postgresql://localhost:5432/pahana_edu_31_96";
        this.username  = "postgres";
        this.password= "postgresdb";

    }

    public static DbConfig getInstance() {
        return dbConfig;
    }

    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
