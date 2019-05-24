package nl.utwente.dao;

import nl.utwente.db.DatabaseConnectionFactory;
import nl.utwente.model.Booking;
import nl.utwente.model.SpecifiedBooking;

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
        Connection connection = DatabaseConnectionFactory.getConnection();
        try {
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
                Date date = resultSet.getDate("date");
                String roomID = resultSet.getString("roomID");

                boolean isprivate = resultSet.getBoolean("getIsPrivate");
                String userID;
                if (isprivate){
                    userID = "PRIVATE";
                } else {
                    userID = resultSet.getString("userID");
                }

                booking = new SpecifiedBooking(startTime, endTime, roomID, date, userID,isprivate);
            }

            resultSet.close();
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

        return booking;
    }

    /**
     * Creates a new booking entry in the database.
     *
     * @return whether the booking was successfully created
     */
    public static boolean createBooking(SpecifiedBooking booking) {
        boolean successful = false;

        if (!isValidBooking(booking)) {
            return false;
        }
        Connection connection = DatabaseConnectionFactory.getConnection();

        try {


//            String findUserIdQuery = "SELECT userID FROM sqills.user WHERE email = ?";


                    String query = "INSERT INTO sqills.Booking (startTime, endTime, date, roomID, \"owner\", isPrivate)\n" +
                    "                VALUES ( ?, ?, ?, ?, \n" +
                    "(SELECT sqills.users.userID\n" +
                    "FROM sqills.users\n" +
                    "WHERE email = ?), ?);";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setTime(1, booking.getStartTime());
            statement.setTime(2, booking.getEndTime());
            statement.setDate(3, booking.getDate());
            statement.setInt(4, Integer.parseInt(booking.getRoomID())); // TODO This should be a string with a subquery for the room name later
            statement.setString(5, booking.getEmail());
            statement.setBoolean(6, booking.getIsPrivate());

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
        Connection connection = DatabaseConnectionFactory.getConnection();
        try {

            String query = "DELETE FROM sqills.Booking WHERE bookingID = ?";
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setInt(1, bookingID);

            int updatedRows = statement.executeUpdate();
            successful = updatedRows > 0;

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
     * Updates a specific booking.
     *
     * @param bookingID specifies the booking to be updated
     * @return whether the update was successful
     */
    // TODO Update this function with private, email fields
    public static boolean updateBooking(int bookingID, SpecifiedBooking booking) {
        boolean successful = false;

        if (!isValidBooking(booking)) {
            return false;
        }
        Connection connection = DatabaseConnectionFactory.getConnection();

        try {

            String query = "UPDATE sqills.Booking " +
                "SET startTime = ?, endTime = ?, date = ?, roomID = ?" +
                "WHERE bookingID = ?";
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setTime(1, booking.getStartTime());
            statement.setTime(2, booking.getEndTime());
            statement.setDate(3, booking.getDate());
            statement.setInt(4, Integer.parseInt(booking.getRoomID())); // TODO room name subquery (see above)
            statement.setInt(5, bookingID);

            int updatedRows = statement.executeUpdate();
            successful = updatedRows > 0;

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
     * Returns a list of today's Bookings for a specified room
     *
     * @param roomID RoomID of room whose bookings will be returned
     * @return Today's bookings for the specified room
     */
    public static List<SpecifiedBooking> getBookingsForRoomToday(int roomID) {
        ArrayList<SpecifiedBooking> result = new ArrayList<>();
        Connection connection = DatabaseConnectionFactory.getConnection();
        try {

            String query = "SELECT b.startTime, b.endTime, b.date, b.roomID, u.name, b.isprivate FROM sqills.booking b JOIN sqills.users u on u.userid = b.owner  WHERE roomID = ? AND date = CURRENT_DATE";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, roomID);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Time startTime = resultSet.getTime("startTime");
                Time endTime = resultSet.getTime("endTime");
                Date date = resultSet.getDate("date");
                int queriedRoomID = resultSet.getInt("roomID");

                // TODO maybe change booking object because right now we are putting stuff in email field that is not an email
                boolean isprivate = resultSet.getBoolean("isprivate");

                String username;
                if (isprivate){
                    username = "PRIVATE";
                } else {
                    username = resultSet.getString("name");
                }


                result.add(new SpecifiedBooking(startTime, endTime, String.valueOf(queriedRoomID), date, username, isprivate));
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


    public static void insertBookingToday(int roomID, Time startTime, Time endTime, String email, boolean isPrivate) {
        Calendar currentTime = Calendar.getInstance();
        Date sqlDate = new Date((currentTime.getTime()).getTime());
        SpecifiedBooking booking = new SpecifiedBooking(startTime, endTime, String.valueOf(roomID), sqlDate,email, isPrivate );
        createBooking(booking);
    }

    public static boolean isValidBookingToday(int roomID, Time startTime, Time endTime) {
        Calendar currentTime = Calendar.getInstance();
        Date sqlDate = new Date((currentTime.getTime()).getTime());
        return isValidBooking(roomID, startTime, endTime, sqlDate);
    }

    public static boolean isValidBooking(int roomID, Time wantedStart, Time wantedEnd, Date date) {
        boolean isValid = true;
        Connection connection = DatabaseConnectionFactory.getConnection();
        try {
            String query = "SELECT starttime, endtime, date FROM Sqills.booking WHERE roomID = ? AND date = ? ";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, roomID);
            statement.setDate(2, date);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                Time bookingStart = Time.valueOf(result.getString("starttime"));
                Time bookingEnd = Time.valueOf(result.getString("endtime"));
//                Time wantedStart = Time.valueOf(startTime);
//                Time wantedEnd = Time.valueOf(endTime);
                // TODO add email checking to this
                if (wantedStart.compareTo(bookingStart) > 0 && wantedStart.compareTo(bookingEnd) < 0
                    || wantedEnd.compareTo(bookingStart) > 0 && wantedEnd.compareTo(bookingEnd) < 0
                    || wantedStart.compareTo(bookingStart) <= 0 && wantedEnd.compareTo(bookingEnd) >= 0
                    ) {
                    isValid = false;
                }
            }
            result.close();
            statement.close();
            connection.close();
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
        return isValid;
    }

    public static boolean isValidBooking(SpecifiedBooking booking) {
        return isValidBooking(Integer.parseInt(booking.getRoomID()),
            booking.getStartTime(),
            booking.getEndTime(),
            booking.getDate());
    }

    // Todo: test
    public List<Integer> getCurrentlyAvailableRooms() {
        List<Integer> ids = new ArrayList<>();
        Connection connection = DatabaseConnectionFactory.getConnection();
        try {
            String query = "SELECT roomid FROM sqills.room " +
                "WHERE roomid NOT IN (" +
                "    SELECT roomid FROM sqills.booking " +
                "    WHERE date = CURRENT_DATE " +
                "    AND CURRENT_TIME BETWEEN starttime AND endtime " +
                ") " +
                "AND roomid > 0;";

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                ids.add(resultSet.getInt("roomid"));
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
        return ids;
    }
}
