package nl.utwente.dao;

import nl.utwente.db.DatabaseConnectionFactory;
import nl.utwente.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ParticipantDao {
    public static List<User> getParticipantIDsOfBooking(int bookingID) {
        Connection connection = DatabaseConnectionFactory.getConnection();
        List<User> result = new ArrayList<>();
        try {
            String query = "SELECT u.userid, u.name, u.email, u.administrator " +
                "FROM sqills.participants AS p, sqills.users AS u " +
                "WHERE p.bookingid = ?" +
                "AND p.userid = u.userid;";

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, bookingID);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                User user = new User();
                user.setUserid(resultSet.getInt("userid"));
                user.setName(resultSet.getString("name"));
                user.setEmail(resultSet.getString("email"));
                user.setAdministrator(resultSet.getBoolean("administrator"));
                result.add(user);
            }
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

    public static boolean addParticipantToBooking(int bookingID, int userID) {
        if (!BookingDao.isValidBookingID(bookingID) || !UserDao.isValidUserID(userID)) {
            return false;
        }

        Connection connection = DatabaseConnectionFactory.getConnection();
        boolean success = false;

        try {
            String query = "INSERT INTO participants.participants (bookingid, userid)" +
                "VALUES (?, ?);";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, bookingID);
            preparedStatement.setInt(2, userID);

            int updatedRows = preparedStatement.executeUpdate();
            success = updatedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return success;
    }
}
