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
    public static List<User> getParticipantsOfBooking(int bookingID) {
        Connection connection = DatabaseConnectionFactory.getConnection();
        List<User> result = new ArrayList<>();
        try {
            String query = "select * from get_participants_of_booking(?)";

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, bookingID);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                User user = new User();
                user.setUserid(resultSet.getInt("user_id"));
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
            String query = "INSERT INTO sqills.participants (bookingid, userid) VALUES (?, ?);";
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

    public static boolean removeParticipant(int bookingID, int userID) {
        Connection connection = DatabaseConnectionFactory.getConnection();
        boolean success = false;

        try {
            String query = "DELETE FROM sqills.participants WHERE bookingid = ? AND userid = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, bookingID);
            preparedStatement.setInt(2, userID);

            int count = preparedStatement.executeUpdate();
            success = count > 0;
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

    // Todo: @Andrew Make into single query
    public static boolean addParticipantEmailToBooking(int bookingID, String email) {
        Connection connection = DatabaseConnectionFactory.getConnection();
        try {
            String useridQuery = "SELECT userid FROM sqills.users WHERE email = ?;";
            PreparedStatement userIDStatement = connection.prepareStatement(useridQuery);
            userIDStatement.setString(1, email);
            ResultSet userIDResult = userIDStatement.executeQuery();
            userIDResult.next();
            final int userID = userIDResult.getInt("userid");

            String insert = "INSERT INTO sqills.participants (bookingid, userid)" +
                "VALUES (?, ?);";
            PreparedStatement insertStatement = connection.prepareStatement(insert);
            insertStatement.setInt(1, bookingID);
            insertStatement.setInt(2, userID);
            insertStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return true;
    }
}
