//package nl.utwente.dao;
//
//import io.bit3.jsass.Output;
//import nl.utwente.db.DatabaseConnectionFactory;
//import nl.utwente.exceptions.BookingException;
//import nl.utwente.exceptions.DAOException;
//import nl.utwente.exceptions.InvalidBookingIDException;
//import nl.utwente.model.Booking;
//import nl.utwente.model.OutputBooking;
//import nl.utwente.model.SpecifiedBooking;
//import org.junit.*;
//
//import java.sql.*;
//import java.time.LocalDate;
//import java.util.List;
//
//import static junit.framework.TestCase.fail;
//import static nl.utwente.dao.BookingDao.*;
//import static nl.utwente.dao.DaoTestUtils.*;
//import static nl.utwente.dao.DaoTestUtils.createRoom;
//import static nl.utwente.dao.DaoTestUtils.deleteRoom;
//import static org.junit.Assert.*;
//
//public class BookingDaoTest {
//    private int bookingID = -1;
//    private Time startTime = currentTimePlusMinutes(2);
//    private Time endTime = new Time(23, 59, 59);
//    private LocalDate localDate = LocalDate.now();
//    private Date date = new Date(localDate.getYear(),localDate.getMonthValue(),localDate.getDayOfMonth());
//    private SpecifiedBooking createdBooking = new SpecifiedBooking(startTime,endTime,roomName, date, email,false,bookingTitle);
//
//    @BeforeClass
//    public static void setup() {
//        connection = DatabaseConnectionFactory.conn;
//    }
//
//    @Before
//    public void beforeEachTest() {
//        createRoom();
//        try {
//            bookingID = createBooking(createdBooking);
//        } catch (BookingException | DAOException e) {
//            e.printStackTrace();
//            fail(e.getMessage());
//        }
//    }
//
//    @Test
//    public void testGetSpecificBookingExistingBooking() {
//        try {
//            OutputBooking booking = getOutputBooking(bookingID);
//            assertEquals(booking.getDate(),date);
//            assertEquals(booking.getStartTime().toString(), "22:00:00");
//            assertEquals(booking.getEndTime().toString(), "23:00:00");
//            assertEquals(booking.getRoomNumber(), 1);
//
//            String deleteQuery = "DELETE FROM sqills.booking " +
//                                 "WHERE bookingid = -1";
//
//            Statement deleteStatement = connection.createStatement();
//            deleteStatement.execute(deleteQuery);
//            deleteStatement.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//            fail();
//        }
//    }
//
//    @Test
//    public void testGetSpecificBookingNonExistingBooking() {
//        OutputBooking booking = null;
//        try {
//            booking = BookingDao.getOutputBooking(-1);
//            assertNull(booking);
//        } catch (InvalidBookingIDException | DAOException e) {
//            e.printStackTrace();
//            fail(e.getMessage());
//        }
//    }
//
//    @Test
//    public void testDeleteBookingNonExisting() {
//        boolean result = BookingDao.deleteBooking(-1);
//        assertFalse(result);
//    }
//
//    @Test
//    public void testValidBooking(){
//        //booking for same time, date room: shoukld not be accepted
//        boolean result1 = BookingDao.isValidSpecifiedBooking(1, "9:00:00", "10:00:00", "2030-12-12");
//        //valid booking with startime just after endtime current booking: should be accepted
//        boolean result2 = BookingDao.isValidSpecifiedBooking(1, "10:00:00", "11:00:00", "2030-12-12");
//        //booking with the current booking in between: should not be accepted
//        boolean result3 = BookingDao.isValidSpecifiedBooking(1, "9:00:00", "11:00:00", "2030-12-12");
//        //other date same time same room: should be accepted
//        boolean result4 = BookingDao.isValidSpecifiedBooking(1, "9:00:00", "11:00:00", "2030-12-13");
//        //other room booking for same time: should be accepted
//        boolean result5 = BookingDao.isValidSpecifiedBooking(2, "9:00:00", "11:00:00", "2030-12-12");
//        //endtime before startime: should not be accepted
//        boolean result6 = BookingDao.isValidSpecifiedBooking(1, "13:00:00", "12:00:00", "2030-12-12");
//        //booking with endtime just before current booking starttime: should be accepted
//        boolean result7 = BookingDao.isValidSpecifiedBooking(1, "8:00:00", "9:00:00", "2030-12-12");
//        assertFalse(result1);
//        assertTrue(result2);
//        assertFalse(result3);
//        assertTrue(result4);
//        assertTrue(result5);
//        assertFalse(result6);
//        assertTrue(result7);
//    }
//
//    @Test
//    public void testCreateBooking() {
//        try {
//            Booking booking = new Booking("11:00:00", "12:00:00", 1, "2020-12-12");
//            createBooking(booking);
//
//            // Check whether the booking was actually created
//            String query = "SELECT * FROM sqills.booking WHERE " +
//                "bookingdate = '2020-12-12' " +
//                "AND startTime = '11:00:00' " +
//                "AND endTime = '12:00:00' " +
//                "AND roomid = 1";
//
//            Statement statement = connection.createStatement();
//            ResultSet rs = statement.executeQuery(query);
//            assertTrue(rs.next());
//            int id = rs.getInt("bookingid");
//            rs.close();
//            statement.close();
//
//            String deleteQuery = "DELETE FROM sqills.booking WHERE bookingid = ?";
//            PreparedStatement ps = connection.prepareStatement(deleteQuery);
//            ps.setInt(1, id);
//            ps.execute();
//            ps.close();
//
//        } catch (SQLException e) {
//            fail();
//        }
//    }
//
//    @Test
//    public void testCreateBookingInvalidBooking() {
//        try {
//            // insert first booking
//            Booking firstBooking = new Booking("11:00:00", "12:00:00", 1, "1999-12-12");
//            createBooking(firstBooking);
//
//            Booking overlappingBooking = new Booking("10:30:00", "13:00:00", 1, "1999-12-12");
//            assertFalse(BookingDao.isValidSpecifiedBooking(overlappingBooking));
//            createBooking(overlappingBooking);
//
//            // Check whether the booking was actually created
//            String query = "SELECT * FROM sqills.booking WHERE " +
//                "bookingdate = '1999-12-12' " +
//                "AND startTime = '10:30:00' " +
//                "AND endTime = '13:00:00' " +
//                "AND roomid = 1";
//
//            Statement statement = connection.createStatement();
//            ResultSet rs = statement.executeQuery(query);
//            assertFalse(rs.next());
//            rs.close();
//            statement.close();
//
//            String deleteQuery = "DELETE FROM sqills.booking WHERE bookingdate = '1999-12-12'";
//            Statement statement2 = connection.createStatement();
//            statement2.execute(deleteQuery);
//            statement2.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//            fail();
//        }
//    }
//
//    @Test
//    public void testUpdateBooking() {
//        try {
//            String query = "INSERT INTO  sqills.booking  (bookingid, starttime, endtime, roomid, bookingdate)" +
//                "VALUES (-1, '11:00:00', '12:00:00', 1, '1999-12-12');";
//            Statement statement = connection.createStatement();
//            statement.execute(query);
//
//
//            Booking updatedBooking = new Booking("12:00:00", "13:00:00", 2, "1999-12-12");
//            BookingDao.updateBooking(-1, updatedBooking);
//
//            Booking result = BookingDao.getSpecificBooking(-1);
//            assertEquals(2, updatedBooking.getRoomNumber());
//            assertEquals(result.getStartTime().toString(), updatedBooking.getStartTime().toString());
//
//            String deleteQuery = "DELETE FROM sqills.booking WHERE bookingdate = '1999-12-12'";
//            Statement statement2 = connection.createStatement();
//            statement2.execute(deleteQuery);
//            statement2.close();
//        } catch (SQLException e) {
//            fail();
//        }
//    }
//
//    @Test
//    public void testUpdateBookingInvalidBooking() {
//        try {
//            String query = "INSERT INTO  sqills.booking  (bookingid, starttime, endtime, roomid, bookingdate)" +
//                "VALUES (-1, '11:00:00', '12:00:00', 1, '1999-12-12');";
//            Statement statement = connection.createStatement();
//            statement.execute(query);
//            statement.close();
//
//            String query2 = "INSERT INTO  sqills.booking  (bookingid, starttime, endtime, roomid, bookingdate)" +
//                "VALUES (-2, '14:00:00', '15:00:00', 1, '1999-12-12');";
//            Statement statement2 = connection.createStatement();
//            statement2.execute(query2);
//
//            Booking booking = new Booking("13:30:00", "16:00:00", 1, "1999-12-12");
//            boolean result = BookingDao.updateBooking(-1, booking);
//
//            String deleteQuery = "DELETE FROM sqills.booking WHERE bookingdate = '1999-12-12'";
//            Statement statement3 = connection.createStatement();
//            statement3.execute(deleteQuery);
//            statement3.close();
//
//            assertFalse(result);
//        } catch (SQLException e) {
//            fail();
//        }
//    }
//
//    @Test
//    public void testGetBookingsForRoomToday() {
//        try {
//            String query = "INSERT INTO  sqills.booking  (bookingid, starttime, endtime, roomid, bookingdate)" +
//                "VALUES (-1, '11:00:00', '12:00:00', 1, CURRENT_DATE);";
//            Statement statement = connection.createStatement();
//            statement.execute(query);
//            statement.close();
//
//            String query2 = "INSERT INTO  sqills.booking  (bookingid, starttime, endtime, roomid, bookingdate)" +
//                "VALUES (-2, '14:00:00', '15:00:00', 1, CURRENT_DATE);";
//            Statement statement2 = connection.createStatement();
//            statement2.execute(query2);
//            statement2.close();
//
//            List<Booking> bookings = BookingDao.getBookingsForRoomToday(1);
//
//            String deleteQuery = "DELETE FROM sqills.booking WHERE bookingid < 0";
//            Statement statement3 = connection.createStatement();
//            statement3.execute(deleteQuery);
//            statement3.close();
//
//            for (Booking booking : bookings) {
//                assertEquals(booking.getRoomNumber(), 1);
//                assertTrue(booking.getStartTime().toString().equals("11:00:00") ||
//                    booking.getStartTime().toString().equals("14:00:00"));
//
//                assertTrue(booking.getEndTime().toString().equals("12:00:00") ||
//                    booking.getEndTime().toString().equals("15:00:00"));
//            }
//        } catch (SQLException e) {
//            fail();
//        }
//    }
//
//    @Test
//    public void testInsertBookingToday() {
//        try {
//            Time startTime = Time.valueOf("01:00:00");
//            Time endTime = Time.valueOf("02:00:00");
//
//            BookingDao.insertBookingToday(-1, startTime, endTime,"antoniaheath@gmail.com",false);
//
//            List<Booking> bookings = BookingDao.getBookingsForRoomToday(-1);
//            assertEquals(bookings.get(0).getStartTime(), startTime);
//            assertEquals(bookings.get(0).getEndTime(), endTime);
//
//            String deleteQuery = "DELETE FROM sqills.booking WHERE roomid < 0";
//            Statement statement3 = connection.createStatement();
//            statement3.execute(deleteQuery);
//            statement3.close();
//        } catch (SQLException e) {
//            fail();
//        }
//    }
//
//    @Test
//    public void testInsertBookingTodayInvalidBooking() {
//        try {
//            Time startTime0 = Time.valueOf("00:30:00");
//            Time endTime0 = Time.valueOf("01:30:00");
//            BookingDao.insertBookingToday(-1, startTime0, endTime0,"antoniaheath@gmail.com",false);
//
//            Time startTime = Time.valueOf("01:00:00");
//            Time endTime = Time.valueOf("02:00:00");
//            BookingDao.insertBookingToday(-1, startTime, endTime,"antoniaheath@gmail.com",false);
//
//            List<Booking> bookings = BookingDao.getBookingsForRoomToday(-1);
//            assertEquals(bookings.get(0).getStartTime(), startTime0);
//            assertEquals(bookings.get(0).getEndTime(), endTime0);
//            assertEquals(bookings.size(), 1);
//
//            String deleteQuery = "DELETE FROM sqills.booking WHERE roomid < 0";
//            Statement statement3 = connection.createStatement();
//            statement3.execute(deleteQuery);
//            statement3.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @After
//    public void afterEachTest() {
//        try {
//            connection.commit();
//            deleteBooking(bookingID);
//            deleteRoom();
//        } catch (SQLException | InvalidBookingIDException | DAOException e) {
//            e.printStackTrace();
//            fail(e.getMessage());
//        }
//    }
//
//    @AfterClass
//    public static void shutDown() {
//        try {
//            connection.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//}
