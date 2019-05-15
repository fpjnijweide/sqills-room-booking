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
    public void setUpConnection() {
        this.connection = DatabaseConnectionFactory.getConnection();
    }

    @After
    public void closeConnection() {
        try {
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
}
