package nl.utwente.dao;

import nl.utwente.db.DatabaseConnectionFactory;

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
        Connection connection = DatabaseConnectionFactory.getConnection();
        try {

            String query = "SELECT * FROM sqills.Room WHERE roomID = ?";

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, roomID);

            ResultSet resultSet = statement.executeQuery();

            boolean result = resultSet.next();
            resultSet.getStatement().getConnection().close();
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
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

    // Todo: test
    public static List<String> getCurrentlyAvailableRooms() {
        List<String> ids = new ArrayList<>();
        try {
            String query = "SELECT roomid FROM sqills.room " +
                "WHERE roomid NOT IN (" +
                "    SELECT roomid FROM sqills.booking " +
                "    WHERE bookingdate = CURRENT_DATE " +
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
                "AND bookingdate = CURRENT_DATE " +
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

    public static List<Booking> getBookingsForThisWeek(int roomID) {
        List<Booking> result = new ArrayList<>();
        String query = "SELECT starttime, endtime, bookingdate " +
            "FROM sqills.booking " +
            "WHERE EXTRACT(WEEK FROM bookingdate) = EXTRACT(WEEK FROM CURRENT_DATE)" +
            "AND roomid = ? " +
            "ORDER BY bookingdate ASC;";

        Connection connection = DatabaseConnectionFactory.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, roomID);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Time startTime = resultSet.getTime("starttime");
                Time endTime = resultSet.getTime("endtime");
                Date date = resultSet.getDate("bookingdate");

                Booking booking = new Booking(startTime, endTime, roomID, date);
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
