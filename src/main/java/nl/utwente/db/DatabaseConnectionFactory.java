package nl.utwente.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnectionFactory {
    public static Connection getConnection(){
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException cnfe) {
            System.err.println("Error loading driver: " + cnfe);
        }
        final String host = "castle.ewi.utwente.nl";
        final String dbName = "di125";
        final String url = "jdbc:postgresql://" + host + ":5432/" + dbName;
        final String username = "di125";
        final String password = "E9+gNMnM";
        try {
            return DriverManager.getConnection(url, username, password);
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
