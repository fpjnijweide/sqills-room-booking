package nl.utwente.dao;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import nl.utwente.db.DatabaseConnectionFactory;
import nl.utwente.model.Booking;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class BookingDao {
    private Time startTime;
    private Time endTime;
    private int roomID;
    private Date date;

    public BookingDao(Time startTime, Time endTime, int roomID, Date date) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.roomID = roomID;
        this.date = date;
    }

    /**
     * Returns a booking with a certain booking id.
     * @param bookingID booking ID of booking to be returned
     * @return returns booking with specified ID or null if the booking does not exist.
     */
    public static Booking getSpecificBooking(int bookingID) {
        Booking booking = null;
        try {
            Connection connection = DatabaseConnectionFactory.getConnection();
            String query = "SELECT * FROM sqills.Booking WHERE bookingID = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, bookingID);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Time startTime = resultSet.getTime("startTime");
                Time endTime = resultSet.getTime("endTime");
                Date date = resultSet.getDate("bookingdate");
                int roomID = resultSet.getInt("roomID");

                booking = new Booking(startTime, endTime, roomID, date);
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return booking;
    }

    /**
     * Creates a new booking entry in the database.
     * @return whether the booking was successfully created
     */
    public static boolean createBooking(Booking booking) {
        boolean successful = false;
        try {
            Connection connection = DatabaseConnectionFactory.getConnection();
            String query = "INSERT INTO sqills.Booking (startTime, endTime, bookingdate, roomID" +
                ") VALUES (?, ?, ?, ?);";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setTime(1, booking.getStartTime());
            statement.setTime(2, booking.getEndTime());
            statement.setDate(3, booking.getDate());
            statement.setInt(4, booking.getRoomNumber());

            int updatedRows = statement.executeUpdate();
            successful = updatedRows > 0;
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return successful;
    }

    /**
     * Deletes a booking with a specified bookingID
     * @param bookingID specifies the booking to be deleted
     * @return whether the deletion was successful
     */
    public static boolean deleteBooking(int bookingID) {
        boolean successful = false;

        try {
            Connection connection = DatabaseConnectionFactory.getConnection();
            String query = "DELETE FROM sqills.Booking WHERE bookingID = ?";
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setInt(1, bookingID);

            int updatedRows = statement.executeUpdate();
            successful = updatedRows > 0;

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return successful;
    }

    /**
     * Updates a specific booking.
     * @param bookingID specifies the booking to be updated
     * @return whether the update was successful
     */
    public static boolean updateBooking(int bookingID, Booking booking) {
        boolean successful = false;

        try {
            Connection connection = DatabaseConnectionFactory.getConnection();
            String query = "UPDATE sqills.Booking " +
                "SET startTime = ?, endTime = ?, bookingdate = ?, roomID = ?" +
                "WHERE bookingID = ?";
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setTime(1, booking.getStartTime());
            statement.setTime(2, booking.getEndTime());
            statement.setDate(3, booking.getDate());
            statement.setInt(4, booking.getRoomNumber());
            statement.setInt(5, bookingID);

            int updatedRows = statement.executeUpdate();
            successful = updatedRows > 0;

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return successful;
    }

    /**
     * Returns a list of today's Bookings for a specified room
     * @param roomID RoomID of room whose bookings will be returned
     * @return Today's bookings for the specified room
     */
    public static List<Booking> getBookingsForRoomToday(int roomID) {
        ArrayList<Booking> result = new ArrayList<>();
        try {
            Connection connection = DatabaseConnectionFactory.getConnection();
            String query = "SELECT startTime, endTime, bookingdate, roomID FROM sqills.booking WHERE roomID = ? AND bookingdate = CURRENT_DATE";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, roomID);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Time startTime = resultSet.getTime("startTime");
                Time endTime = resultSet.getTime("endTime");
                Date date = resultSet.getDate("bookingdate");
                int queriedRoomID = resultSet.getInt("roomID");
                result.add(new Booking(startTime, endTime, queriedRoomID, date));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void insertBookingToday(int roomID, Time startTime, Time endTime) {
        try {
            Connection connection = DatabaseConnectionFactory.getConnection();
            String query = "INSERT INTO sqills.Booking (" +
                "roomID," +
                "startTime," +
                "endtime," +
                "bookingdate" +
            ") VALUES (" +
                "?," +
                "?," +
                "?," +
                "CURRENT_DATE" +
            ")";

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, roomID);
            statement.setTime(2, startTime);
            statement.setTime(3, endTime);
            statement.execute();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean isValidBookingToday(int roomID, String startTime, String endTime) {
        Calendar currenttime = Calendar.getInstance();
        Date sqldate = new Date((currenttime.getTime()).getTime());
        return isValidBooking(roomID, startTime, endTime, sqldate.toString());
    }

    public static boolean isValidBooking(int roomID, String startTime, String endTime, String date) {
        boolean isValid = true;
        Connection connection = DatabaseConnectionFactory.getConnection();
        try {
            String query = "SELECT starttime, endtime, bookingdate FROM Sqills.booking WHERE roomID = ? AND bookingdate = ? ";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, roomID);
            statement.setDate(2, Date.valueOf(date));
            ResultSet result = statement.executeQuery();
            while(result.next()){
                Time start = Time.valueOf(result.getString("starttime"));
                Time end = Time.valueOf(result.getString("endtime"));
                Time wantedStart = Time.valueOf(startTime);
                Time wantedEnd = Time.valueOf(endTime);
                if (wantedStart.compareTo(start) > 0 && wantedStart.compareTo(end) < 0
                    || wantedEnd.compareTo(start) > 0 && wantedEnd.compareTo(end) < 0
                    || wantedStart.compareTo(start) <= 0 && wantedEnd.compareTo(end) >= 0){
                    isValid = false;
                }
            }
            result.close();
            statement.close();
            connection.close();
        } catch(SQLException e){
            e.printStackTrace();
            return false;
        }
        return isValid;
    }

    public Time getStartTime() {
        return startTime;
    }

    public Time getEndTime() {
        return endTime;
    }

    public int getRoomID() {
        return roomID;
    }

    public Date getDate() {
        return date;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("BookingDao from: ");
        sb.append(startTime);
        sb.append("-");
        sb.append(endTime);
        return sb.toString();
    }

    public ObjectNode toJSONNode() {
        final JsonNodeFactory factory = JsonNodeFactory.instance;
        ObjectNode bookingJsonNode = factory.objectNode();
        bookingJsonNode.put("roomNumber", this.roomID);
        bookingJsonNode.put("startTime", String.valueOf(this.startTime));
        bookingJsonNode.put("endTime", String.valueOf(this.endTime));
        bookingJsonNode.put("date", String.valueOf(this.date));
        return bookingJsonNode;
    }
}
