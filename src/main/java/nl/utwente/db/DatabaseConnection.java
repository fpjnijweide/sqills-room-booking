package nl.utwente.db;

import java.sql.*;

public class DatabaseConnection {
    private static Connection getConnection(){
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException cnfe) {
            System.err.println("Error loading driver: " + cnfe);
        }
        String host = "castle.ewi.utwente.nl";
        String dbName = "di125";
        String url = "jdbc:postgresql://" + host + ":5432/" + dbName;
        String username = "di125";
        String password = "E9+gNMnM";
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            return connection;
        } catch(SQLException sqle) {
            System.err.println("Error connecting: " + sqle);
        }
        return null;
    }

    public static ResultSet sendQuery(String query) {
        try {
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(query);
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void closeResultSet(ResultSet resultSet) {
        try {
            resultSet.getStatement().getConnection().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String query = "select * from sqills.Room;";
        ResultSet rs = DatabaseConnection.sendQuery(query);
        try {
            while (rs.next()) {
                System.out.println(rs.getString("roomID"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DatabaseConnection.closeResultSet(rs);
    }
}
