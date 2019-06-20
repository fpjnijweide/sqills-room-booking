//package nl.utwente.dao;
//
//import nl.utwente.db.DatabaseConnectionFactory;
//import nl.utwente.model.Booking;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//
//import java.sql.*;
//import java.util.List;
//
//import static junit.framework.TestCase.fail;
//import static org.junit.Assert.*;
//
//public class BookingDaoTest {
//    private Connection connection;
//
//    @Before
//    public void setUpConnectionAndPopulate() {
//        this.connection = DatabaseConnectionFactory.getConnection();
//
//        try {
//            Statement statement = connection.createStatement();
//            String query = "INSERT INTO Sqills.Booking(bookingdate, starttime, endtime, roomID) " +
//                           "VALUES ('2030-12-12', '9:00:00', '10:00:00', 1)";
//            int updatedRows = statement.executeUpdate(query);
//        } catch(SQLException sqle) {
//            System.err.println(sqle);
//        }
//    }
//
//    @After
//    public void closeConnection() {
//        try {
//            Statement statement = connection.createStatement();
//            String query = "DELETE FROM sqills.Booking WHERE bookingdate = '2030-12-12'";
//            int updatedRows = statement.executeUpdate(query);
//            statement.close();
//            this.connection.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    public void testDeleteBookingExistingBooking() {
//        try {
//            // Insert a query with ID of -1
//            String insertQuery = "INSERT INTO sqills.booking " +
//                                 "(bookingid, bookingdate, starttime, endtime, roomid) " +
//                                 "VALUES " +
//                                 "(-1, '2069-01-01', '22:00:00', '23:00:00', 1);";
//
//            Statement insertStatement = connection.createStatement();
//            insertStatement.execute(insertQuery);
//            insertStatement.close();
//
//            boolean result = BookingDao.deleteBooking(-1);
//            assertTrue(result);
//
//            String selectQuery = "SELECT * FROM sqills.booking WHERE bookingid = -1;";
//            Statement selectStatement = connection.createStatement();
//            ResultSet resultSet = selectStatement.executeQuery(selectQuery);
//
//            assertFalse(resultSet.next());
//
//            resultSet.close();
//            selectStatement.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//            fail();
//        }
//    }
//
//    @Test
//    public void testGetSpecificBookingExistingBooking() {
//        try {
//            String insertQuery = "INSERT INTO sqills.booking " +
//                                 "(bookingid, bookingdate, starttime, endtime, roomid) " +
//                                 "VALUES " +
//                                 "(-1, '2069-01-01', '22:00:00', '23:00:00', 1);";
//
//            Statement insertStatement = connection.createStatement();
//            insertStatement.execute(insertQuery);
//            insertStatement.close();
//
//            Booking booking = BookingDao.getSpecificBooking(-1);
//            assertEquals(booking.getDate().toString(), ("2069-01-01"));
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
//        Booking booking = BookingDao.getSpecificBooking(-1);
//        assertNull(booking);
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
//            BookingDao.createBooking(booking);
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
//            BookingDao.createBooking(firstBooking);
//
//            Booking overlappingBooking = new Booking("10:30:00", "13:00:00", 1, "1999-12-12");
//            assertFalse(BookingDao.isValidSpecifiedBooking(overlappingBooking));
//            BookingDao.createBooking(overlappingBooking);
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
//}
