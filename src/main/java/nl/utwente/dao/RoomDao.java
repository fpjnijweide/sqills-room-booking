package nl.utwente.dao;

import nl.utwente.db.DatabaseConnectionFactory;
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
    public static boolean isValidRoomID(int roomID) {
        return getRoomName(roomID) != null;
    }

    public static String getRoomName(int roomID) {
        String result = null;
        Connection connection = DatabaseConnectionFactory.getConnection();
        try {
            String query = "SELECT roomname FROM sqills.Room WHERE roomid = ?";

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, roomID);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                result = resultSet.getString("roomname");
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

    public static int getRoomID(String roomName) {
        int result = -1;
        Connection connection = DatabaseConnectionFactory.getConnection();
        try {

            String query = "SELECT roomid FROM sqills.Room WHERE roomName = ?";

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, roomName);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                result = resultSet.getInt("roomid");
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

    public static boolean isValidRoomName(String roomName) {
        return getRoomID(roomName) != -1;
    }

    public static List<Integer> getAllRoomsIDs() {
        ArrayList<Integer> result = new ArrayList<>();
        Connection connection = DatabaseConnectionFactory.getConnection();
        try {

            String query = "SELECT roomid FROM sqills.room";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String queriedRoomID = resultSet.getString("roomid");
                result.add(Integer.parseInt(queriedRoomID));
            }

            resultSet.close();
            statement.close();
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

    public static List<String> getAllRoomNames() {
        ArrayList<String> result = new ArrayList<>();
        Connection connection = DatabaseConnectionFactory.getConnection();
        try {

            String query = "SELECT roomname FROM sqills.room";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String queriedRoomName = resultSet.getString("roomname");
                result.add(queriedRoomName);
            }

            resultSet.close();
            statement.close();
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

    public static List<String> getCurrentlyAvailableRooms() {
        List<String> rooms = new ArrayList<>();
        Connection connection = DatabaseConnectionFactory.getConnection();
        try {
            String query = "select * from get_currently_available_rooms()";

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                rooms.add(resultSet.getString("roomname"));
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
        return rooms;
    }

    public static Time getFreeUntil(String roomName) throws InvalidRoomNameException {
        if (!isValidRoomName(roomName)){
            throw new InvalidRoomNameException(roomName);
        }
        Time result = null;

        try {
            String query = "SELECT * from get_free_until(?)";
            Connection connection = DatabaseConnectionFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, roomName);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                result = resultSet.getTime(1);
            }

            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return result;
        }
        return result;
    }

    public static List<OutputBooking> getBookingsForThisWeek(String roomName) throws InvalidRoomNameException {
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
                OutputBooking booking = resultSetToBooking(roomName, resultSet);
                result.add(booking);
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


}
