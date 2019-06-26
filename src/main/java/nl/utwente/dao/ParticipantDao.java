package nl.utwente.dao;

import nl.utwente.db.DatabaseConnectionFactory;
import nl.utwente.exceptions.DAOException;
import nl.utwente.exceptions.InvalidBookingIDException;
import nl.utwente.exceptions.InvalidEmailException;
import nl.utwente.exceptions.InvalidUserIDException;
import nl.utwente.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static nl.utwente.dao.BookingDao.isValidBookingID;
import static nl.utwente.dao.UserDao.isValidEmail;

public class ParticipantDao {
    public static List<User> getParticipantsOfBooking(int bookingID) throws InvalidBookingIDException, DAOException {
        if (!isValidBookingID(bookingID)){
            throw new InvalidBookingIDException(bookingID);
        }
        Connection connection = DatabaseConnectionFactory.conn;
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
throw new DAOException(e.getMessage());
        } finally {
            try {
                connection.commit();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static void addParticipantToBooking(int bookingID, int userID) throws InvalidBookingIDException, InvalidUserIDException, DAOException {
        if (!isValidBookingID(bookingID)){
            throw new InvalidBookingIDException(bookingID);
        }
        if ( !UserDao.isValidUserID(userID)) {
            throw new InvalidUserIDException(userID);
        }

        Connection connection = DatabaseConnectionFactory.conn;

        try {
            String query = "INSERT INTO sqills.participants (booking_id, user_id) VALUES (?, ?);";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, bookingID);
            preparedStatement.setInt(2, userID);

            int updatedRows = preparedStatement.executeUpdate();
            if (updatedRows == 0){
                throw new DAOException("Somehing went wrong in deleteParticipantsOfBooking()");
            }
        } catch (SQLException e) {
            e.printStackTrace();
throw new DAOException(e.getMessage());
        } finally {
            try {
                connection.commit();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    public static void removeParticipant(int bookingID, int userID) throws InvalidBookingIDException, InvalidUserIDException, DAOException {
        if (!isValidBookingID(bookingID)){
            throw new InvalidBookingIDException(bookingID);
        }
        if ( !UserDao.isValidUserID(userID)) {
            throw new InvalidUserIDException(userID);
        }
        Connection connection = DatabaseConnectionFactory.conn;

        try {
            String query = "DELETE FROM sqills.participants WHERE booking_id = ? AND user_id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, bookingID);
            preparedStatement.setInt(2, userID);

            int updatedRows = preparedStatement.executeUpdate();
            if (updatedRows == 0){
                throw new DAOException("Somehing went wrong in deleteParticipantsOfBooking()");
            }
        } catch (SQLException e) {
            e.printStackTrace();
throw new DAOException(e.getMessage());
        } finally {
            try {
                connection.commit();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Todo: @Andrew Make into single query
    public static void addParticipantEmailToBooking(int bookingID, String email) throws InvalidBookingIDException, InvalidEmailException, DAOException {
        if (!isValidBookingID(bookingID)){
            throw new InvalidBookingIDException(bookingID);
        }

        if ( !isValidEmail(email)) {
            throw new InvalidEmailException(email);
        }


        Connection connection = DatabaseConnectionFactory.conn;
        try {
            String useridQuery = "SELECT user_id FROM sqills.users WHERE email = ?;";
            PreparedStatement userIDStatement = connection.prepareStatement(useridQuery);
            userIDStatement.setString(1, email);
            ResultSet userIDResult = userIDStatement.executeQuery();
            userIDResult.next();
            final int userID = userIDResult.getInt("user_id");

            String insert = "INSERT INTO sqills.participants (booking_id, user_id)" +
                "VALUES (?, ?);";
            PreparedStatement insertStatement = connection.prepareStatement(insert);
            insertStatement.setInt(1, bookingID);
            insertStatement.setInt(2, userID);
            int updatedRows = insertStatement.executeUpdate();
            if (updatedRows == 0){
                throw new DAOException("Somehing went wrong in deleteParticipantsOfBooking()");
            }
        } catch (SQLException e) {
            e.printStackTrace();
throw new DAOException(e.getMessage());
        } finally {
            try {
                connection.commit();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }
}
