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
        List<User> participants = new ArrayList<>();
        Connection connection = null;
        try{
            connection = DatabaseConnectionFactory.getConnection();
        String participantQuery = "SELECT u.user_id, u.name, u.email, u.administrator " +
            "FROM sqills.users AS u, sqills.participants AS p " +
            "WHERE p.booking_id = ? " +
            "AND u.user_id = p.user_id";
        PreparedStatement preparedStatement = connection.prepareStatement(participantQuery);
        preparedStatement.setInt(1, bookingID);
        ResultSet resultSetParticipants = preparedStatement.executeQuery();

        while (resultSetParticipants.next()) {
            User user = new User();
            user.setUserid(resultSetParticipants.getInt("user_id"));
            user.setName(resultSetParticipants.getString("name"));
            user.setEmail(resultSetParticipants.getString("email"));
            user.setAdministrator(resultSetParticipants.getBoolean("administrator"));
            participants.add(user);
        }

        resultSetParticipants.close();
        preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException(e.getMessage());
        } finally {
            try {
                connection.commit();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return participants;
    }

    public static void addParticipantToBooking(int bookingID, int userID) throws InvalidBookingIDException, InvalidUserIDException, DAOException {
        Connection connection = null;
        try {
            connection = DatabaseConnectionFactory.getConnection();

            if (!isValidBookingID(bookingID, connection)) {
                throw new InvalidBookingIDException(bookingID);
            }
            if (!UserDao.isValidUserID(userID, connection)) {
                throw new InvalidUserIDException(userID);
            }

            String query = "INSERT INTO sqills.participants (booking_id, user_id) VALUES (?, ?);";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, bookingID);
            preparedStatement.setInt(2, userID);

            int updatedRows = preparedStatement.executeUpdate();
            if (updatedRows == 0) {
                throw new DAOException("Somehing went wrong in deleteParticipantsOfBooking()");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException(e.getMessage());
        } finally {
            try {
                connection.commit();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    public static void removeParticipant(int bookingID, int userID) throws InvalidBookingIDException, InvalidUserIDException, DAOException {


        Connection connection = null;
        try {
            connection = DatabaseConnectionFactory.getConnection();

            if (!isValidBookingID(bookingID, connection)) {
                throw new InvalidBookingIDException(bookingID);
            }
            if (!UserDao.isValidUserID(userID, connection)) {
                throw new InvalidUserIDException(userID);
            }

            String query = "DELETE FROM sqills.participants WHERE booking_id = ? AND user_id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, bookingID);
            preparedStatement.setInt(2, userID);

            int updatedRows = preparedStatement.executeUpdate();
            if (updatedRows == 0) {
                throw new DAOException("Somehing went wrong in deleteParticipantsOfBooking()");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException(e.getMessage());
        } finally {
            try {
                connection.commit();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Todo: @Andrew Make into single query
    public static void addParticipantEmailToBooking(int bookingID, String email) throws InvalidBookingIDException, InvalidEmailException, DAOException {


        if (!isValidEmail(email)) {
            throw new InvalidEmailException(email);
        }


        Connection connection = null;
        try {
            connection = DatabaseConnectionFactory.getConnection();
            if (!isValidBookingID(bookingID, connection)) {
                throw new InvalidBookingIDException(bookingID);
            }
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
            if (updatedRows == 0) {
                throw new DAOException("Somehing went wrong in deleteParticipantsOfBooking()");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException(e.getMessage());
        } finally {
            try {
                connection.commit();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean userParticipatesInBooking(int bookingID, int userID) {
        boolean result = false;
        Connection connection = null;
        try {
            connection = DatabaseConnectionFactory.getConnection();

            String query = "SELECT * FROM sqills.participants " +
                "WHERE booking_id = ? AND user_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, bookingID);
            preparedStatement.setInt(2, userID);

            ResultSet resultSet = preparedStatement.executeQuery();
            result = resultSet.next();
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
