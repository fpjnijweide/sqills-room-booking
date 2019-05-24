package nl.utwente.dao;

import nl.utwente.db.DatabaseConnectionFactory;
import nl.utwente.model.SpecifiedBooking;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomDao {
    /**
     * Returns whether a roomID belongs to a room.
     * @param roomID The roomID which validity will be checked
     * @return Whether the provided roomID is valid
     */
    public static boolean isValidRoomID(int roomID) {
       return getRoomName(roomID)!=null;
    }

    public static String getRoomName(int roomID){
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

    public static int getRoomID(String roomName){
        int result=-1;
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
        return getRoomID(roomName)!=-1;
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
                String queriedRoomName = resultSet.getString("roomid");
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

    // Todo: test
    public static List<String> getCurrentlyAvailableRoomIDs() {
        List<String> ids = new ArrayList<>();
        try {
            String query = "SELECT roomid FROM sqills.room " +
                "WHERE roomid NOT IN (" +
                "    SELECT roomid FROM sqills.booking " +
                "    WHERE date = CURRENT_DATE " +
                "    AND CURRENT_TIME BETWEEN starttime AND endtime " +
                ") " +
                "AND roomid > 0;";
            Connection connection = DatabaseConnectionFactory.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                ids.add(resultSet.getString("roomid"));
            }

            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return ids;
        }
        return ids;
    }

    public static Time getFreeUntil(int roomID) {
        Time result = null;

        try {
            String query = "SELECT MIN(starttime) FROM sqills.booking " +
                "WHERE roomid = ? " +
                "AND date = CURRENT_DATE " +
                "AND starttime > CURRENT_TIME;";
            Connection connection = DatabaseConnectionFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, roomID);

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

    public static List<SpecifiedBooking> getBookingsForThisWeek(int roomID) {
        List<SpecifiedBooking> result = new ArrayList<>();
        String query = "SELECT starttime, endtime, date, owner, isPrivate " +
            "FROM sqills.booking " +
            "WHERE EXTRACT(WEEK FROM date) = EXTRACT(WEEK FROM CURRENT_DATE)" +
            "AND roomid = ? " +
            "ORDER BY date ASC;";

        Connection connection = DatabaseConnectionFactory.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, roomID);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Time startTime = resultSet.getTime("starttime");
                Time endTime = resultSet.getTime("endtime");
                Date date = resultSet.getDate("date");
                String email = resultSet.getString("owner");
                boolean isPrivate = resultSet.getBoolean("isPrivate");

                SpecifiedBooking booking = new SpecifiedBooking(startTime, endTime, roomID, date, email, isPrivate);
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
