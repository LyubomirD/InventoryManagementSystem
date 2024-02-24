package com.example.ims.db_connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    public Connection databaseLink;

    private static final String databaseUsername = "lubodimoff";
    private static final String databasePassword = "";
    private static final String databaseURL = "jdbc:postgresql://localhost:5555/inventorymanagementsystem";


    public Connection getConnection() {
        try {
            Class.forName("org.postgresql.Driver");
            databaseLink = DriverManager.getConnection(databaseURL, databaseUsername, databasePassword);

        } catch (SQLException e) {
            e.getStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("PostgreSQL JDBC Driver not found", e);
        }

        return databaseLink;
    }
}
