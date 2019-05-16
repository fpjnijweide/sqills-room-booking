package nl.utwente.dao;

import nl.utwente.db.DatabaseConnectionFactory;
import nl.utwente.model.Booking;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.*;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.*;

public class BookingDaoTest {
    private Connection connection;

    @Before
    public void setUpConnectionAndPopulate() {
        this.connection = DatabaseConnectionFactory.getConnection();

        try {
            Statement statement = connection.createStatement();
            String query = "INSERT INTO Sqills.Booking(bookingdate, starttime, endtime, roomID) " +
                           "VALUES ('2030-12-12', '9:00:00', '10:00:00', 1)";
            int updatedRows = statement.executeUpdate(query);
        } catch(SQLException sqle) {
            System.err.println(sqle);
        }
    }

    @After
    public void closeConnection() {
        try {
            Statement statement = connection.createStatement();
            String query = "DELETE FROM sqills.Booking WHERE bookingdate = '2030-12-12'";
            int updatedRows = statement.executeUpdate(query);
            statement.close();
            this.connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDeleteBookingExistingBooking() {
        try {
            // Insert a query with ID of -1
            String insertQuery = "INSERT INTO sqills.booking " +
                                 "(bookingid, bookingdate, starttime, endtime, roomid) " +
                                 "VALUES " +
                                 "(-1, '2069-01-01', '22:00:00', '23:00:00', 1);";

            Statement insertStatement = connection.createStatement();
            insertStatement.execute(insertQuery);
            insertStatement.close();

            boolean result = BookingDao.deleteBooking(-1);
            assertTrue(result);

            String selectQuery = "SELECT * FROM sqills.booking WHERE bookingid = -1;";
            Statement selectStatement = connection.createStatement();
            ResultSet resultSet = selectStatement.executeQuery(selectQuery);

            assertFalse(resultSet.next());

            resultSet.close();
            selectStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testGetSpecificBookingExistingBooking() {
        try {
            String insertQuery = "INSERT INTO sqills.booking " +
                                 "(bookingid, bookingdate, starttime, endtime, roomid) " +
                                 "VALUES " +
                                 "(-1, '2069-01-01', '22:00:00', '23:00:00', 1);";

            Statement insertStatement = connection.createStatement();
            insertStatement.execute(insertQuery);
            insertStatement.close();

            Booking booking = BookingDao.getSpecificBooking(-1);
            assertEquals(booking.getDate().toString(), ("2069-01-01"));
            assertEquals(booking.getStartTime().toString(), "22:00:00");
            assertEquals(booking.getEndTime().toString(), "23:00:00");
            assertEquals(booking.getRoomNumber(), 1);

            String deleteQuery = "DELETE FROM sqills.booking " +
                                 "WHERE bookingid = -1";

            Statement deleteStatement = connection.createStatement();
            deleteStatement.execute(deleteQuery);
            deleteStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testGetSpecificBookingNonExistingBooking() {
        Booking booking = BookingDao.getSpecificBooking(-1);
        assertNull(booking);
    }

    @Test
    public void testDeleteBookingNonExisting() {
        boolean result = BookingDao.deleteBooking(-1);
        assertFalse(result);
    }

    @Test
    public void testValidBooking(){
        //booking for same time, date room: shoukld not be accepted
        boolean result1 = BookingDao.isValidBooking(1, "9:00:00", "10:00:00", "2030-12-12");
        //valid booking with startime just after endtime current booking: should be accepted
        boolean result2 = BookingDao.isValidBooking(1, "10:00:00", "11:00:00", "2030-12-12");
        //booking with the current booking in between: should not be accepted
        boolean result3 = BookingDao.isValidBooking(1, "9:00:00", "11:00:00", "2030-12-12");
        //other date same time same room: should be accepted
        boolean result4 = BookingDao.isValidBooking(1, "9:00:00", "11:00:00", "2030-12-13");
        //other room booking for same time: should be accepted
        boolean result5 = BookingDao.isValidBooking(2, "9:00:00", "11:00:00", "2030-12-12");
        //endtime before startime: should not be accepted
        boolean result6 = BookingDao.isValidBooking(1, "13:00:00", "12:00:00", "2030-12-12");
        //booking with endtime just before current booking starttime: should be accepted
        boolean result7 = BookingDao.isValidBooking(1, "8:00:00", "9:00:00", "2030-12-12");
        assertFalse(result1);
        assertTrue(result2);
        assertFalse(result3);
        assertTrue(result4);
        assertTrue(result5);
        assertFalse(result6);
        assertTrue(result7);
    }
}
