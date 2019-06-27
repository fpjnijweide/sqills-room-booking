 package nl.utwente.dao;

import nl.utwente.db.DatabaseConnectionFactory;
import nl.utwente.exceptions.*;
import nl.utwente.model.*;

import javax.ws.rs.core.SecurityContext;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import org.apache.poi.util.Internal;

import static nl.utwente.dao.ParticipantDao.getParticipantsOfBooking;
import static nl.utwente.dao.RoomDao.isValidRoomName;
import static nl.utwente.dao.UserDao.getUserFromEmail;
import static nl.utwente.dao.UserDao.isValidEmail;
import static nl.utwente.db.DatabaseConnectionFactory.*;

public class BookingDao {
    /**
     * Returns a booking with a certain booking id.
     *
     * @param bookingID booking ID of booking to be returned
     * @return returns booking with specified ID or null if the booking does not exist.
     */
    public static OutputBooking getOutputBooking(int bookingID) throws InvalidBookingIDException, DAOException {

        OutputBookingWithParticipants booking = new OutputBookingWithParticipants();

        Connection connection = null;
        try {
            connection = DatabaseConnectionFactory.getConnection();

            if (!isValidBookingID(bookingID, connection)){
                throw new InvalidBookingIDException(bookingID);
            }

            String query = "SELECT * from get_specific_booking(?)";

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, bookingID);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                booking.setRoomName(resultSet.getString("room_name"));
                booking.setTitle(resultSet.getString("title"));
                booking.setDate(resultSet.getDate("date"));
                booking.setStartTime(resultSet.getTime("start_time"));
                booking.setEndTime(resultSet.getTime("end_time"));
                booking.setUserName(resultSet.getString("name"));
                booking.setBookingid(bookingID);
            }

            resultSet.close();
            statement.close();

            String participantQuery = "SELECT u.user_id, u.name, u.email, u.administrator " +
                "FROM sqills.users AS u, sqills.participants AS p " +
                "WHERE p.booking_id = ? " +
                "AND u.user_id = p.user_id";
            PreparedStatement preparedStatement = connection.prepareStatement(participantQuery);
            preparedStatement.setInt(1, bookingID);
            ResultSet resultSetParticipants = preparedStatement.executeQuery();

            List<User> participants = new ArrayList<>();
            while (resultSetParticipants.next()) {
                User user = new User();
                user.setUserid(resultSetParticipants.getInt("user_id"));
                user.setName(resultSetParticipants.getString("name"));
                user.setEmail(resultSetParticipants.getString("email"));
                user.setAdministrator(resultSetParticipants.getBoolean("administrator"));
                participants.add(user);
            }
            booking.setParticipants(participants);

            resultSetParticipants.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
throw new DAOException(e.getMessage());
        } finally {
            try {
                connection.commit();
connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return booking;
    }

    public static String getEmailOfBookingOwner(int bookingID) throws InvalidBookingIDException, DAOException {

        String email = null;

        Connection connection = null;
        try {

            connection = DatabaseConnectionFactory.getConnection();
            if (!isValidBookingID(bookingID, connection)){
                throw new InvalidBookingIDException(bookingID);
            }
            String query = "SELECT u.email " +
                "FROM sqills.Booking b " +
                "    JOIN sqills.users u ON u.user_id = b.owner " +
                "WHERE b.booking_id = ?";

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, bookingID);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                email = resultSet.getString("email");
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
throw new DAOException(e.getMessage());
        } finally {
            try {
                connection.commit();
connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return email;
    }

    /**
     * Creates a new booking entry in the database.
     *
     * @return id of booking
     */
    public static int createBooking(SpecifiedBooking booking) throws BookingException, DAOException {
        int id = -1;


        Connection connection = null;
        try {
            connection = DatabaseConnectionFactory.getConnection();
            throwSpecifiedBookingExceptions(booking, connection);

            String query = "select * from create_booking(?,?,?,?,?,?,?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setTime(1, booking.getStartTime());
            statement.setTime(2, booking.getEndTime());
            statement.setDate(3, booking.getDate());
            statement.setString(4, booking.getRoomName());
            statement.setString(5, booking.getEmail());
            statement.setBoolean(6, booking.getIsPrivate());
            statement.setString(7, booking.getTitle());


            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            id = resultSet.getInt(1);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
throw new DAOException(e.getMessage());
        } finally {
            try {
                connection.commit();
connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return id;
    }

    public static SpecifiedBooking prepareBooking(SecurityContext securityContext, SpecifiedBooking booking) {
        boolean loggedIn = securityContext.getUserPrincipal() != null;
        if (Objects.equals(booking.getEmail(), "") || booking.getEmail()==null) {
            if (loggedIn) {
                booking.setEmail(securityContext.getUserPrincipal().getName());
            } else {
                booking.setEmail("sqills_tablet@gmail.com");
            }
        }
        return booking;
    }

    /**
     * Create a recurring specified booking
     */
    public static int createRecurringBooking(RecurringBooking booking) throws BookingException, DAOException {


        int id = -1;

        Connection connection = null;
        try {
            connection = DatabaseConnectionFactory.getConnection();
            throwSpecifiedBookingExceptions(booking, connection);

            String query = "select create_recurring_booking_parent(?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setTime(1, booking.getStartTime());
            statement.setTime(2, booking.getEndTime());
            statement.setDate(3, booking.getDate());
            statement.setString(4, booking.getRoomName());
            statement.setString(5, booking.getEmail());
            statement.setBoolean(6, booking.getIsPrivate());
            statement.setString(7, booking.getTitle());
            statement.setObject(8, booking.getRepeatEveryType(), java.sql.Types.OTHER);
            statement.setInt(9,booking.getRepeatEvery());
            statement.setDate(10,booking.getEndingAt());

            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            id = resultSet.getInt(1);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
throw new DAOException(e.getMessage());
        } finally {
            try {
                connection.commit();
connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return id;

    }
    /**
     * Deletes particiapnts of booking with a specified bookingID
     *
     * @param bookingID specifies the booking to be deleted
     * @return whether the deletion was successful
     */
    @Internal
    static void deleteParticipantsOfBooking(int bookingID, Connection connection) throws InvalidBookingIDException, DAOException {


        try {
            if (!isValidBookingID(bookingID, connection)){
                throw new InvalidBookingIDException(bookingID);
            }

            String query = "DELETE FROM sqills.participants WHERE booking_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setInt(1, bookingID);

            int updatedRows = statement.executeUpdate();

            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
throw new DAOException(e.getMessage());
        }

    }


    /**
     * Deletes a booking with a specified bookingID
     *
     * @param bookingID specifies the booking to be deleted
     * @return whether the deletion was successful
     */
    public static void deleteBooking(int bookingID) throws InvalidBookingIDException, DAOException {

        Connection connection = null;
        try {
            connection = DatabaseConnectionFactory.getConnection();

            if (!isValidBookingID(bookingID, connection)){
                throw new InvalidBookingIDException(bookingID);
            }

            deleteParticipantsOfBooking(bookingID, connection);

            String query = "DELETE FROM sqills.Booking WHERE booking_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setInt(1, bookingID);

            int updatedRows = statement.executeUpdate();
            if (updatedRows == 0){
                throw new DAOException("Somehing went wrong in deleteParticipantsOfBooking()");
            }

            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
throw new DAOException(e.getMessage());
        } finally {
            try {
                connection.commit();
connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }
    /**
     * Updates a specific booking.
     *
     * @param bookingID specifies the booking to be updated
     * @return whether the update was successful
     */
    public static void updateBooking(int bookingID, SpecifiedBooking booking) throws BookingException, InvalidBookingIDException, DAOException {


        Connection connection = null;
        try {
            connection = DatabaseConnectionFactory.getConnection();

            if (!isValidBookingID(bookingID, connection)){
                throw new InvalidBookingIDException(bookingID);
            }

            throwSpecifiedBookingExceptions(booking, connection);

            String query = "select update_booking(?,?,?,?,?,?,?,?)";

            PreparedStatement statement = connection.prepareStatement(query);

            statement.setTime(1, booking.getStartTime());
            statement.setTime(2, booking.getEndTime());
            statement.setDate(3, booking.getDate());
            statement.setString(4, booking.getRoomName());
            statement.setString(5, booking.getEmail());
            statement.setBoolean(6, booking.getIsPrivate());
            statement.setString(7, booking.getTitle());
            statement.setInt(8, bookingID);

            int updatedRows = statement.executeUpdate();
            if (updatedRows == 0){
                throw new DAOException("Somehing went wrong in deleteParticipantsOfBooking()");
            }

            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
throw new DAOException(e.getMessage());
        } finally {
            try {
                connection.commit();
connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Returns a list of today's Bookings for a specified room
     *
     * @param roomName of room whose bookings will be returned
     * @return Today's bookings for the specified room
     */
    public static List<OutputBooking> getBookingsForRoomToday(String roomName) throws InvalidRoomNameException, DAOException {
        return getBookingsForRoomToday(roomName, null);
    }

    public static List<OutputBooking> getBookingsForRoomToday(String roomName, String email) throws InvalidRoomNameException, DAOException {

        ArrayList<OutputBooking> result = new ArrayList<>();

        Connection connection = null;
        try {
            connection = DatabaseConnectionFactory.getConnection();

            if (!isValidRoomName(roomName, connection)){
                throw new InvalidRoomNameException(roomName);
            }

            String query = "select * from booking_for_room_today(?)";

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, roomName);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                OutputBooking booking = resultSetToBooking(roomName, resultSet, email);
                result.add(booking);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
throw new DAOException(e.getMessage());
        } finally {
            try {
                connection.commit();
connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static OutputBooking resultSetToBooking(String roomName, ResultSet resultSet) throws SQLException, DAOException {
        return resultSetToBooking(roomName, resultSet, null);
    }

    public static OutputBooking resultSetToBooking(String roomName, ResultSet resultSet, String email) throws SQLException, DAOException {
        Time startTime = resultSet.getTime("start_time");
        Time endTime = resultSet.getTime("end_time");
        Date date = resultSet.getDate("date");
        int bookingid = resultSet.getInt("booking_id");

        boolean isPrivate = resultSet.getBoolean("is_private");

        String userName = "PRIVATE";
        String title = "PRIVATE";

        try {
            if (!isPrivate || (Objects.equals(email, getEmailOfBookingOwner(bookingid))) ||
                getParticipantsOfBooking(bookingid).contains(getUserFromEmail(email)) || getUserFromEmail(email).isAdministrator())  {
                 // User owns booking, or participates in it, or it is not private

                userName = resultSet.getString("name");
                title = resultSet.getString("title");
            }

        } catch (InvalidBookingIDException | InvalidEmailException ignored) {
        }
        return new OutputBooking(startTime, endTime, userName, roomName, date, title, bookingid);
    }


    public static int insertBookingToday(String roomName, Time startTime, Time endTime, String email, boolean isPrivate, String title) throws BookingException, DAOException {
        Calendar currentTime = Calendar.getInstance();
        Date sqlDate = new Date((currentTime.getTime()).getTime());
        SpecifiedBooking booking = new SpecifiedBooking(startTime, endTime, roomName, sqlDate, email, isPrivate, title);
        int bookingID= createBooking(booking);
        return bookingID;
    }

    @Internal
    static boolean isValidSpecifiedBooking(SpecifiedBooking booking, Connection connection) throws DAOException {
        return isValidBooking(booking, booking.getRoomName(), booking.getDate(), connection);
    }

    @Internal
    static boolean isValidBookingToday(Booking booking, String roomName, Connection connection) throws DAOException {
        Calendar currentTime = Calendar.getInstance();
        Date sqlDate = new Date((currentTime.getTime()).getTime());
        return isValidBooking(booking, roomName, sqlDate, connection);
    }

    @Internal
    static boolean isValidBooking(Booking booking, String roomName, Date sqlDate, Connection connection) throws DAOException {
        boolean validEmail = isValidEmail(booking.getEmail());
        boolean validTimeSlot = false;
        try {
            validTimeSlot = isValidTimeSlot(roomName, booking.getStartTime(), booking.getEndTime(), sqlDate, connection);
        } catch (InvalidRoomNameException e) {
            return false;
        }
        return (validEmail && validTimeSlot);
    }

    @Internal
    static void throwSpecifiedBookingExceptions(SpecifiedBooking booking, Connection connection) throws BookingException, DAOException {
        throwBookingExceptions(booking, booking.getRoomName(), booking.getDate(), connection);
    }

    @Internal
    static void throwBookingTodayExceptions(Booking booking, String roomName, Connection connection) throws BookingException, DAOException {
        Calendar currentTime = Calendar.getInstance();
        Date sqlDate = new Date((currentTime.getTime()).getTime());
        throwBookingExceptions(booking, roomName, sqlDate, connection);
    }

    @Internal
    static void throwBookingExceptions(Booking booking, String roomName, Date sqlDate, Connection connection) throws BookingException, DAOException {
        ArrayList<String> errorMessages = new ArrayList<>();

        if (!isValidTitle(booking.getTitle())) {
            errorMessages.add("Invalid title");
        }

        if (!isValidEmail(booking.getEmail())) {
            errorMessages.add("Invalid email");
        }

        try {
            boolean validTimeSlot = isValidTimeSlot(roomName, booking.getStartTime(), booking.getEndTime(), sqlDate, connection);
            if (!validTimeSlot) {
                errorMessages.add("Booking overlaps or time is invalid");
            }
        } catch (InvalidRoomNameException e) {
            errorMessages.add("Invalid room name");
        }

        if (!errorMessages.isEmpty()){
            String errorString = String.join("\n",errorMessages);
            throw new BookingException(errorString);
        }
    }

    @Internal
    static boolean isValidTimeSlot(String roomName, Time wantedStart, Time wantedEnd, Date date, Connection connection) throws InvalidRoomNameException, DAOException {
        if (!isValidRoomName(roomName, connection)){
            throw new InvalidRoomNameException(roomName);
        }
        boolean isValid = true;

        try {
            String query = "select * from get_bookings_on_date_in_room(?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, roomName);
            statement.setDate(2, date);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                Time bookingStart = Time.valueOf(result.getString("start_time"));
                Time bookingEnd = Time.valueOf(result.getString("end_time"));
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
        }
        return isValid;
    }

    @Internal
    static boolean isValidBooking(SpecifiedBooking booking, Connection connection) throws DAOException {
        return isValidBooking(booking,
            booking.getRoomName(),
            booking.getDate(), connection);
    }

    @Internal
    static boolean isValidBookingID(int bookingID, Connection connection) throws DAOException {

        boolean isValid = false;

        try {
            String query = "SELECT * FROM sqills.booking WHERE booking_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, bookingID);

            ResultSet resultSet = preparedStatement.executeQuery();
            isValid = resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
throw new DAOException(e.getMessage());
        }

        return isValid;
    }

    public static List<OutputBooking> getFilteredBookings(String email, String title, Date startDate, Date endDate) throws DAOException {
        List<OutputBooking> bookings = new ArrayList<>();
        String query = "SELECT b.booking_id, b.title, u.email, u.name, b.start_time, b.end_time, r.room_name, b.date " +
            "FROM sqills.booking as b, sqills.users as u, sqills.room as r " +
            "WHERE b.owner = u.user_id " +
            "AND r.room_id = b.room_id " +
            "AND b.title ILIKE CONCAT('%', ?, '%') " +
            "AND u.email ILIKE CONCAT('%', ?, '%') " +
            "AND b.date >= ? " +
            "AND b.date <= ?";



        Connection connection = null;
        try {
            connection = DatabaseConnectionFactory.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, title);
            preparedStatement.setString(2, email);
            preparedStatement.setDate(3, startDate);
            preparedStatement.setDate(4, endDate);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                OutputBooking booking = new OutputBooking();
                booking.setTitle(resultSet.getString("title"));
                booking.setBookingid(resultSet.getInt("booking_id"));
                booking.setRoomName(resultSet.getString("room_name"));
                booking.setDate(resultSet.getDate("date"));
                booking.setStartTime(resultSet.getTime("start_time"));
                booking.setEndTime(resultSet.getTime("end_time"));
                booking.setUserName(resultSet.getString("name"));
                bookings.add(booking);
            }
        } catch (SQLException e){
            e.printStackTrace();
            throw new DAOException("Internal server exceptions");
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return bookings;
    }

    public static boolean isValidTitle(String title) {
        final String ALLOWED_CHARS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ,. -_";

        for (int i = 0; i < title.length(); i++){
            char c = title.charAt(i);

            if (ALLOWED_CHARS.indexOf(c) == -1) {
                return false;
            }
        }

        return true;
    }
}
