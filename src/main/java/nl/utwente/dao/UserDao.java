package nl.utwente.dao;

import nl.utwente.db.DatabaseConnectionFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDao {
    public static List<String> getAllUserMails() {
        ArrayList<String> result = new ArrayList<>();
        Connection connection = DatabaseConnectionFactory.getConnection();
        try {

            String query = "SELECT email FROM sqills.users";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String email = resultSet.getString("email");
                result.add(email);
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
        return result;
    }
}
