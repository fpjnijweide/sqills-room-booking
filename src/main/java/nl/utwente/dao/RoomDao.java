package nl.utwente.dao;

import nl.utwente.db.DatabaseConnectionFactory;
import nl.utwente.exceptions.DAOException;
import nl.utwente.exceptions.InvalidRoomNameException;
import nl.utwente.model.OutputBooking;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static nl.utwente.dao.BookingDao.resultSetToBooking;

public class RoomDao {
    /**
     * Returns whether a roomID belongs to a room.
     *
     * @param roomID The roomID which validity will be checked
     * @return Whether the provided roomID is valid
     */
    public static boolean isValidRoomID(int roomID) throws DAOException {
        return getRoomName(roomID) != null;
    }

    public static String getRoomName(int roomID) throws DAOException {
        String result = null;
        Connection connection = DatabaseConnectionFactory.getConnection();
        try {
            String query = "SELECT room_name FROM sqills.Room WHERE room_id = ?";

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, roomID);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                result = resultSet.getString("room_name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
throw new DAOException(e.getMessage());
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static int getRoomID(String roomName) throws DAOException {
        int result = -1;
        Connection connection = DatabaseConnectionFactory.getConnection();
        try {

            String query = "SELECT room_id FROM sqills.Room WHERE room_name ilike ?";

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, roomName);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                result = resultSet.getInt("room_id");
            }

        } catch (SQLException e) {
            e.printStackTrace();
throw new DAOException(e.getMessage());
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static boolean isValidRoomName(String roomName) throws DAOException {
        return getRoomID(roomName) != -1;
    }

    public static List<Integer> getAllRoomsIDs() throws DAOException {
        ArrayList<Integer> result = new ArrayList<>();
        Connection connection = DatabaseConnectionFactory.getConnection();
        try {

            String query = "SELECT room_id FROM sqills.room";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String queriedRoomID = resultSet.getString("room_id");
                result.add(Integer.parseInt(queriedRoomID));
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
throw new DAOException(e.getMessage());
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static List<String> getAllRoomNames() throws DAOException {
        ArrayList<String> result = new ArrayList<>();
        Connection connection = DatabaseConnectionFactory.getConnection();
        try {

            String query = "SELECT room_name FROM sqills.room";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String queriedRoomName = resultSet.getString("room_name");
                result.add(queriedRoomName);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
throw new DAOException(e.getMessage());
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static List<String> getCurrentlyAvailableRooms() throws DAOException {
        List<String> rooms = new ArrayList<>();
        Connection connection = DatabaseConnectionFactory.getConnection();
        try {
            String query = "select * from get_currently_available_rooms()";

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                rooms.add(resultSet.getString("room_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
throw new DAOException(e.getMessage());
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return rooms;
    }

    public static Time getFreeUntil(String roomName) throws InvalidRoomNameException, DAOException {
        if (!isValidRoomName(roomName)){
            throw new InvalidRoomNameException(roomName);
        }
        Time result = null;
        Connection connection = DatabaseConnectionFactory.getConnection();
        try {
            String query = "SELECT * from get_free_until(?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, roomName);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                result = resultSet.getTime(1);
            }

            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
throw new DAOException(e.getMessage());
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static List<OutputBooking> getBookingsForThisWeek(String roomName) throws InvalidRoomNameException, DAOException {
        return getBookingsForThisWeek(roomName, null);
    }

    public static List<OutputBooking> getBookingsForThisWeek(String roomName, String email) throws InvalidRoomNameException, DAOException {
        if (!isValidRoomName(roomName)){
            throw new InvalidRoomNameException(roomName);
        }

        List<OutputBooking> result = new ArrayList<>();
        String query = "SELECT * from get_booking_for_this_week(?)";

        Connection connection = DatabaseConnectionFactory.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, roomName);
                ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                OutputBooking booking = resultSetToBooking(roomName, resultSet,email);
                result.add(booking);
            }
        } catch (SQLException e) {
            e.printStackTrace();
throw new DAOException(e.getMessage());
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
