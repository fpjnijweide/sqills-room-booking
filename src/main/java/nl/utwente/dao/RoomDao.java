package nl.utwente.dao;

import nl.utwente.db.DatabaseConnectionFactory;
import nl.utwente.model.OutputBooking;
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
            String query = "SELECT roomname FROM sqills.room " +
                "WHERE roomid NOT IN (" +
                "    SELECT roomid FROM sqills.booking " +
                "    WHERE date = CURRENT_DATE " +
                "    AND CURRENT_TIME BETWEEN starttime AND endtime " +
                ") " +
                "AND roomid > 0;";

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

    // TODO if this function is here, why do we do the incredibly stupid "getEarliestStartTime" thing on front end?
    public static Time getFreeUntil(String roomName) {
        Time result = null;

        try {
            String query = "SELECT MIN(b.starttime) FROM sqills.booking b " +
                "JOIN sqills.room r ON b.roomid = r.roomid " +
                "WHERE r.roomname = ? " +
                "AND b.date = CURRENT_DATE " +
                "AND b.starttime > CURRENT_TIME;";
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
    public static List<OutputBooking> getBookingsForThisWeek(String roomName) {
        // TODO maybe check if valid room name here? we already do that somewhere else? idk
        List<OutputBooking> result = new ArrayList<>();
        String query = "SELECT b.starttime, b.endtime, b.date, u.name, b.isPrivate " +
            "FROM sqills.booking b " +
            "JOIN sqills.room r ON b.roomid = r.roomid " +
            "JOIN sqills.users u ON b.owner = u.userid " +
            "WHERE EXTRACT(WEEK FROM date) = EXTRACT(WEEK FROM CURRENT_DATE)" +
            "AND r.roomname = ? " +
            "ORDER BY b.date ASC;";

        Connection connection = DatabaseConnectionFactory.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, roomName);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Time startTime = resultSet.getTime("startTime");
                Time endTime = resultSet.getTime("endTime");
                Date date = resultSet.getDate("date");


                boolean isprivate = resultSet.getBoolean("isPrivate");

                String userName;
                if (isprivate) {
                    userName = "PRIVATE";
                } else {
                    userName = resultSet.getString("name");
                }


                result.add(new OutputBooking(startTime, endTime, userName, roomName, date));
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
