package nl.utwente.dao;

import nl.utwente.db.DatabaseConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {
    public static boolean isValidUserID(int userID) {
        Connection connection = DatabaseConnectionFactory.getConnection();
        boolean isValid = false;

        try {
            String query = "SELECT * FROM sqills.users WHERE userid = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userID);

            ResultSet resultSet = preparedStatement.executeQuery();
            isValid = resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return isValid;
    }
    public static String getEmail(String text) {
        int count = 0;
        String email = null;
        Connection connection = DatabaseConnectionFactory.getConnection();
        try {
            String query = "SELECT email FROM sqills.users WHERE email LIKE ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, text + "%");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                count++;
                email = resultSet.getString("email");
            }
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (count == 1){
            return email;
        }
        return null;
    }
}
