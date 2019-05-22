package nl.utwente.dao;

import nl.utwente.db.DatabaseConnectionFactory;
import nl.utwente.model.Booking;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class BookingDao {
    /**
     * Returns a booking with a certain booking id.
     *
     * @param bookingID booking ID of booking to be returned
     * @return returns booking with specified ID or null if the booking does not exist.
     */
    public static Booking getSpecificBooking(int bookingID) {
        Booking booking = null;
        try {
            Connection connection = DatabaseConnectionFactory.getConnection();
            String query = "SELECT * FROM sqills.Booking WHERE bookingID = ?";
//            String query = "SELECT *" +
//                " FROM sqills.Booking B " +
//                "JOIN sqills.user U ON B.userid = U.userid" +
//                "WHERE B.bookingID = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, bookingID);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Time startTime = resultSet.getTime("startTime");
                Time endTime = resultSet.getTime("endTime");
                Date date = resultSet.getDate("bookingdate");
                int roomID = resultSet.getInt("roomID");

                boolean isprivate = resultSet.getBoolean("isPrivate");
                String userID;
                if (isprivate){
                    userID = "PRIVATE";
                } else {
                    userID = resultSet.getString("userID");
                }

                booking = new Booking(startTime, endTime, roomID, date, userID,isprivate);
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
     *
     * @return whether the booking was successfully created
     */
    public static boolean createBooking(Booking booking) {
        boolean successful = false;

        if (!isValidBooking(booking)) {
            return false;
        }

        try {


//            String findUserIdQuery = "SELECT userID FROM sqills.user WHERE email = ?";


                String query = "INSERT INTO sqills.Booking (startTime, endTime, bookingdate, roomID, userID, isPrivate)\n" +
                    "                VALUES ( ?, ?, ?, ?, \n" +
                    "(SELECT sqills.users.userID\n" +
                    "FROM sqills.users\n" +
                    "WHERE email = ?), ?);";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setTime(1, booking.getStartTime());
            statement.setTime(2, booking.getEndTime());
            statement.setDate(3, booking.getDate());
            statement.setInt(4, booking.getRoomNumber());
            statement.setString(5, booking.getEmail());
            statement.setBoolean(6, booking.isPrivate());

            int updatedRows = statement.executeUpdate();
            successful = updatedRows > 0;
            // TODO maybe have a nice error if e-mail is not found in database
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();


        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return successful;
    }

    /**
     * Deletes a booking with a specified bookingID
     *
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

            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return successful;
    }

    /**
     * Updates a specific booking.
     *
     * @param bookingID specifies the booking to be updated
     * @return whether the update was successful
     */
    // TODO Update this function with private, email fields
    public static boolean updateBooking(int bookingID, Booking booking) {
        boolean successful = false;

        if (!isValidBooking(booking)) {
            return false;
        }

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

            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return successful;
    }

    /**
     * Returns a list of today's Bookings for a specified room
     *
     * @param roomID RoomID of room whose bookings will be returned
     * @return Today's bookings for the specified room
     */
    public static List<Booking> getBookingsForRoomToday(int roomID) {
        ArrayList<Booking> result = new ArrayList<>();
        try {
            Connection connection = DatabaseConnectionFactory.getConnection();
            String query = "SELECT startTime, endTime, bookingdate, roomID, userID FROM sqills.booking WHERE roomID = ? AND bookingdate = CURRENT_DATE";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, roomID);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Time startTime = resultSet.getTime("startTime");
                Time endTime = resultSet.getTime("endTime");
                Date date = resultSet.getDate("bookingdate");
                int queriedRoomID = resultSet.getInt("roomID");

                // TODO maybe change booking object because right now we are putting stuff in email field that is not an email
                boolean isprivate = resultSet.getBoolean("isPrivate");

                String userID;
                if (isprivate){
                    userID = "PRIVATE";
                } else {
                    userID = resultSet.getString("userID");
                }


                result.add(new Booking(startTime, endTime, queriedRoomID, date, userID, isprivate));
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    // TODO Freek: don't touch this, add parameters when merging with Marten
    public static void insertBookingToday(int roomID, Time startTime, Time endTime) {
        Calendar currentTime = Calendar.getInstance();
        Date sqlDate = new Date((currentTime.getTime()).getTime());
        Booking booking = new Booking(startTime, endTime, roomID, sqlDate);
        createBooking(booking);
    }

    public static boolean isValidBookingToday(int roomID, String startTime, String endTime) {
        Calendar currentTime = Calendar.getInstance();
        Date sqlDate = new Date((currentTime.getTime()).getTime());
        return isValidBooking(roomID, startTime, endTime, sqlDate.toString());
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
            while (result.next()) {
                Time start = Time.valueOf(result.getString("starttime"));
                Time end = Time.valueOf(result.getString("endtime"));
                Time wantedStart = Time.valueOf(startTime);
                Time wantedEnd = Time.valueOf(endTime);
                // TODO add email checking to this
                if (wantedStart.compareTo(start) > 0 && wantedStart.compareTo(end) < 0
                    || wantedEnd.compareTo(start) > 0 && wantedEnd.compareTo(end) < 0
                    || wantedStart.compareTo(start) <= 0 && wantedEnd.compareTo(end) >= 0
                    || startTime.compareTo(endTime) >= 0) {
                    isValid = false;
                }
            }
            result.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return isValid;
    }

    public static boolean isValidBooking(Booking booking) {
        return isValidBooking(booking.getRoomNumber(),
            booking.getStartTime().toString(),
            booking.getEndTime().toString(),
            booking.getDate().toString());
    }

    // Todo: test
    public List<Integer> getCurrentlyAvailableRooms() {
        List<Integer> ids = new ArrayList<>();
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
                ids.add(resultSet.getInt("roomid"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return ids;
        }
        return ids;
    }
}
