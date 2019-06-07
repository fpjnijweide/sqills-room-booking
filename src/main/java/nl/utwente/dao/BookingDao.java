package nl.utwente.dao;

import nl.utwente.db.DatabaseConnectionFactory;
import nl.utwente.model.Booking;
import nl.utwente.model.OutputBooking;
import nl.utwente.model.SpecifiedBooking;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static nl.utwente.dao.RoomDao.isValidRoomName;
import static nl.utwente.dao.UserDao.isValidEmail;

public class BookingDao {
    /**
     * Returns a booking with a certain booking id.
     *
     * @param bookingID booking ID of booking to be returned
     * @return returns booking with specified ID or null if the booking does not exist.
     */
    public static OutputBooking getSpecificBooking(int bookingID) {
        if (!isValidBookingID(bookingID)){
            return null;
        }
        OutputBooking booking = null;
        Connection connection = DatabaseConnectionFactory.getConnection();
        try {
            String query = "SELECT b.starttime, b.endtime, u.name, r.roomname, b.date, b.isprivate, b.title " +
                "FROM sqills.Booking b " +
                "    JOIN sqills.room r ON b.roomid = r.roomid " +
                "    JOIN sqills.users u ON u.userid = b.owner " +
                "WHERE b.bookingid = ?";

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, bookingID);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Time startTime = resultSet.getTime("startTime");
                Time endTime = resultSet.getTime("endTime");
                Date date = resultSet.getDate("date");
                String roomName = resultSet.getString("roomName");

                boolean isPrivate = resultSet.getBoolean("isPrivate");

                String userName;
                String title;
                if (isPrivate) {
                    userName = "PRIVATE";
                    title = "PRIVATE";
                } else {
                    userName = resultSet.getString("name");
                    title = resultSet.getString("title");
                }

                booking = new OutputBooking(startTime, endTime, userName, roomName, date, title);
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

        return booking;
    }

    /**
     * Creates a new booking entry in the database.
     *
     * @return id of booking
     */
    public static int createBooking(SpecifiedBooking booking) {
        int id = -1;

        if (!isValidSpecifiedBooking(booking)) {
            return id;
        }
        Connection connection = DatabaseConnectionFactory.getConnection();

        try {
            String query = "INSERT INTO sqills.Booking (startTime, endTime, date, roomID, \"owner\", isPrivate, title) " +
                "                VALUES ( ?, ?, ?, " +
                "(SELECT sqills.room.roomid " +
                "FROM sqills.room " +
                "WHERE roomname = ?), " +
                "(SELECT sqills.users.userID " +
                "FROM sqills.users " +
                "WHERE email = ?), " +
                " ?, " +
                "?) " +
                "RETURNING bookingid;";
            PreparedStatement statement = prepareBookingStatement(booking, connection, query);



            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            id = resultSet.getInt("bookingid");
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

        return id;
    }

    private static PreparedStatement prepareBookingStatement(SpecifiedBooking booking, Connection connection, String query) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setTime(1, booking.getStartTime());
        statement.setTime(2, booking.getEndTime());
        statement.setDate(3, booking.getDate());
        statement.setString(4, booking.getRoomName());
        statement.setString(5, booking.getEmail());
        statement.setBoolean(6, booking.getIsPrivate());
        statement.setString(7, booking.getTitle());
        return statement;
    }

    /**
     * Deletes a booking with a specified bookingID
     *
     * @param bookingID specifies the booking to be deleted
     * @return whether the deletion was successful
     */
    public static boolean deleteBooking(int bookingID) {
        if (!isValidBookingID(bookingID)){
            return false;
        }
        boolean successful = false;
        Connection connection = DatabaseConnectionFactory.getConnection();
        try {

            String query = "DELETE FROM sqills.Booking WHERE bookingID = ?";
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setInt(1, bookingID);

            int updatedRows = statement.executeUpdate();
            successful = updatedRows > 0;

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

        if (!isValidSpecifiedBooking(booking)) {
            return false;
        }
        Connection connection = DatabaseConnectionFactory.getConnection();

        try {
            String query = "UPDATE sqills.Booking " +
                "SET startTime=?, " +
                "endTime=?, " +
                "date=?, " +
                "roomID= (SELECT sqills.room.roomid " +
                "FROM sqills.room " +
                "WHERE roomname = ?), " +
                "\"owner\"= (SELECT sqills.users.userID " +
                "FROM sqills.users " +
                "WHERE email = ?), " +
                "isPrivate=?, " +
                "title=? " +
                "WHERE bookingID = ?;";

            PreparedStatement statement = prepareBookingStatement(booking, connection, query);
            statement.setInt(8, bookingID);

            int updatedRows = statement.executeUpdate();
            successful = updatedRows > 0;

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

        return successful;
    }

    /**
     * Returns a list of today's Bookings for a specified room
     *
     * @param roomName of room whose bookings will be returned
     * @return Today's bookings for the specified room
     */
    public static List<OutputBooking> getBookingsForRoomToday(String roomName) {
        if (!isValidRoomName(roomName)){
            return null;
        }
        ArrayList<OutputBooking> result = new ArrayList<>();
        Connection connection = DatabaseConnectionFactory.getConnection();
        try {
            String query = "SELECT b.starttime, b.endtime, u.name, b.date, b.isprivate, b.title\n" +
                "FROM sqills.Booking b\n" +
                "    JOIN sqills.room r ON b.roomid = r.roomid\n" +
                "    JOIN sqills.users u ON u.userid = b.owner\n" +
                "WHERE r.roomname = ? AND b.date = CURRENT_DATE";

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, roomName);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                OutputBooking booking = resultSetToBooking(roomName, resultSet);
                result.add(booking);
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

    public static OutputBooking resultSetToBooking(String roomName, ResultSet resultSet) throws SQLException {
        Time startTime = resultSet.getTime("startTime");
        Time endTime = resultSet.getTime("endTime");
        Date date = resultSet.getDate("date");

        boolean isPrivate = resultSet.getBoolean("isPrivate");

        String userName;
        String title;
        if (isPrivate) {
            userName = "PRIVATE";
            title = "PRIVATE;";
        } else {
            userName = resultSet.getString("name");
            title = resultSet.getString("title");
        }
        return new OutputBooking(startTime, endTime, userName, roomName, date, title);
    }


    public static int insertBookingToday(String roomName, Time startTime, Time endTime, String email, boolean isPrivate, String title) {
        Calendar currentTime = Calendar.getInstance();
        Date sqlDate = new Date((currentTime.getTime()).getTime());
        SpecifiedBooking booking = new SpecifiedBooking(startTime, endTime, roomName, sqlDate, email, isPrivate, title);
        int bookingID= createBooking(booking);
        return bookingID;
    }

    public static boolean isValidSpecifiedBooking(SpecifiedBooking booking) {
        return isValidBooking(booking, booking.getRoomName(), booking.getDate());
    }

    public static boolean isValidBookingToday(Booking booking, String roomName){
        Calendar currentTime = Calendar.getInstance();
        Date sqlDate = new Date((currentTime.getTime()).getTime());
        return isValidBooking(booking, roomName, sqlDate);
    }

    public static boolean isValidBooking(Booking booking, String roomName, Date sqlDate) {
        boolean validEmail = isValidEmail(booking.getEmail());
        boolean validTimeSlot =  isValidTimeSlot(roomName, booking.getStartTime(), booking.getEndTime(), sqlDate);
        return (validEmail && validTimeSlot);
    }

    public static boolean isValidTimeSlot(String roomName, Time wantedStart, Time wantedEnd, Date date) {
        if (!isValidRoomName(roomName)){
            return false;
        }
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
                if (wantedStart.compareTo(bookingStart) > 0 && wantedStart.compareTo(bookingEnd) < 0
                    || wantedEnd.compareTo(bookingStart) > 0 && wantedEnd.compareTo(bookingEnd) < 0
                    || wantedStart.compareTo(bookingStart) <= 0 && wantedEnd.compareTo(bookingEnd) >= 0
                ) {
                    isValid = false;
                }
            }
            result.close();
            statement.close();
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

    public static boolean isValidBookingID(int bookingID) {
        Connection connection = DatabaseConnectionFactory.getConnection();
        boolean isValid = false;

        try {
            String query = "SELECT * FROM sqills.booking WHERE bookingid = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, bookingID);

            ResultSet resultSet = preparedStatement.executeQuery();
            isValid = resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return isValid;
    }
}
