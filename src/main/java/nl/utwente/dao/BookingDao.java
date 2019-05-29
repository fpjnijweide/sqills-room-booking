package nl.utwente.dao;

import nl.utwente.db.DatabaseConnectionFactory;
import nl.utwente.model.Booking;
import nl.utwente.model.OutputBooking;
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
    public static OutputBooking getSpecificBooking(int bookingID) {
        OutputBooking booking = null;
        Connection connection = DatabaseConnectionFactory.getConnection();
        try {


            String query = "SELECT b.starttime, b.endtime, u.name, r.roomname, b.date, b.isprivate\n" +
                "FROM sqills.Booking b\n" +
                "    JOIN sqills.room r ON b.roomid = r.roomid\n" +
                "    JOIN sqills.users u ON u.userid = b.owner\n" +
                "WHERE b.bookingid = ?";
//
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, bookingID);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Time startTime = resultSet.getTime("b.startTime");
                Time endTime = resultSet.getTime("b.endTime");
                Date date = resultSet.getDate("b.date");
                String roomName = resultSet.getString("r.roomname");


                boolean isprivate = resultSet.getBoolean("b.isPrivate");

                String userName;
                if (isprivate) {
                    userName = "PRIVATE";
                } else {
                    userName = resultSet.getString("u.name");
                }


                booking = new OutputBooking(startTime, endTime, userName, roomName, date);
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

            // TODO check if this works
            // TODO @Andrew change this into SQL functions for higher marks!
            String query = "INSERT INTO sqills.Booking (startTime, endTime, date, roomID, \"owner\", isPrivate)\n" +
                "                VALUES ( ?, ?, ?, \n" +
                "(SELECT sqills.room.roomid\n" +
                "FROM sqills.room\n" +
                "WHERE roomname = ?),\n" +
                "(SELECT sqills.users.userID\n" +
                "FROM sqills.users\n" +
                "WHERE email = ?),\n" +
                " ?);";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setTime(1, booking.getStartTime());
            statement.setTime(2, booking.getEndTime());
            statement.setDate(3, booking.getDate());
            statement.setString(4, booking.getRoomName());
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
    public static boolean updateBooking(int bookingID, SpecifiedBooking booking) {
        boolean successful = false;

        if (!isValidBooking(booking)) {
            return false;
        }
        Connection connection = DatabaseConnectionFactory.getConnection();

        try {
            String query =  "UPDATE sqills.Booking \n" +
                            "SET startTime=?, " +
                            "endTime=?, " +
                            "date=?, " +
                            "roomID= (SELECT sqills.room.roomid\n" +
                                     "FROM sqills.room\n" +
                                     "WHERE roomname = ?),\n" +
                            "\"owner\"= (SELECT sqills.users.userID\n" +
                                        "FROM sqills.users\n" +
                                        "WHERE email = ?),\n" +
                            "isPrivate=? \n" +
                            "WHERE bookingID = ?;";

            PreparedStatement statement = connection.prepareStatement(query);

            statement.setTime(1, booking.getStartTime());
            statement.setTime(2, booking.getEndTime());
            statement.setDate(3, booking.getDate());
            statement.setString(4, booking.getRoomName());
            statement.setString(5, booking.getEmail());
            statement.setBoolean(6, booking.getIsPrivate());
            statement.setInt(7, bookingID);

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
     * @param roomName of room whose bookings will be returned
     * @return Today's bookings for the specified room
     */
    // TODO Check if this works
    public static List<OutputBooking> getBookingsForRoomToday(String roomName) {
        ArrayList<OutputBooking> result = new ArrayList<>();
        Connection connection = DatabaseConnectionFactory.getConnection();
        try {

            String query = "SELECT b.starttime, b.endtime, u.name, b.date, b.isprivate\n" +
                "FROM sqills.Booking b\n" +
                "    JOIN sqills.room r ON b.roomid = r.roomid\n" +
                "    JOIN sqills.users u ON u.userid = b.owner\n" +
                "WHERE r.roomname = ? AND b.date = CURRENT_DATE";
//

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


    public static void insertBookingToday(String roomName, Time startTime, Time endTime, String email, boolean isPrivate) {
        Calendar currentTime = Calendar.getInstance();
        Date sqlDate = new Date((currentTime.getTime()).getTime());
        SpecifiedBooking booking = new SpecifiedBooking(startTime, endTime, roomName, sqlDate, email, isPrivate);
        createBooking(booking);
    }

    public static boolean isValidBookingToday(String roomName, Time startTime, Time endTime) {
        Calendar currentTime = Calendar.getInstance();
        Date sqlDate = new Date((currentTime.getTime()).getTime());
        return isValidBooking(roomName, startTime, endTime, sqlDate);
    }

    public static boolean isValidBooking(String roomName, Time wantedStart, Time wantedEnd, Date date) {
        boolean isValid = true;
        Connection connection = DatabaseConnectionFactory.getConnection();
        try {
            String query = "SELECT b.starttime, b.endtime, b.date FROM Sqills.booking b \n" +
                "JOIN sqills.room r ON b.roomid=r.roomid\n" +
                "WHERE r.roomname = ? AND b.date = ? ";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, roomName);
            statement.setDate(2, date);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                Time bookingStart = Time.valueOf(result.getString("starttime"));
                Time bookingEnd = Time.valueOf(result.getString("endtime"));
//                Time wantedStart = Time.valueOf(startTime);
//                Time wantedEnd = Time.valueOf(endTime);
                // TODO add email checking to this and such? right now only checks if the time is valid
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
        return isValidBooking(booking.getRoomName(),
            booking.getStartTime(),
            booking.getEndTime(),
            booking.getDate());
    }

}
