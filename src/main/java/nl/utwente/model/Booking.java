package nl.utwente.model;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import nl.utwente.db.DatabaseConnectionFactory;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Booking {
    private Time startTime;
    private Time endTime;
    private int roomID;
    private Date date;

    public Booking(Time startTime, Time endTime, int roomID, Date date) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.roomID = roomID;
        this.date = date;
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

    public static boolean isValidBookingToday(int roomID, String startTime, String endTime){
        boolean isValid = true;
        Connection connection = DatabaseConnectionFactory.getConnection();
        try {
            String query = "SELECT starttime, endtime, bookingdate from Sqills.booking where roomID = ? and bookingdate = CURRENT_DATE ";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, roomID);
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
        } catch(SQLException e){
            System.err.println(e);
            return false;
        }
        return isValid;
    }

    public static java.sql.Date fromStringToDate(String stringDate){
        java.sql.Date sqlDate = null;
        try {
            java.util.Date date = new SimpleDateFormat("yyyy-MM-dd").parse(stringDate);
            sqlDate = new java.sql.Date(date.getTime());
        } catch (ParseException e){
            System.err.println(e);
        }
        return sqlDate;
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
        sb.append("Booking from: ");
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

    public static void main(String[] args) {
        List<Booking> bookings = getBookingsForRoomToday(1);
        for (Booking b : bookings) {
            System.out.println(b.toJSONNode().toString());
        }
    }
}
