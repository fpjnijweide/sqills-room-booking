package nl.utwente;
import java.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import nl.utwente.dao.BookingDao;
import nl.utwente.db.DatabaseConnectionFactory;
import org.junit.Before;
import org.junit.Test;

import javax.validation.constraints.AssertFalse;

import static nl.utwente.dao.BookingDao.isValidBooking;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class BoookingDaoTest {
    @Before
    public void populateDatabase(){
        Connection connection = DatabaseConnectionFactory.getConnection();
        try {
            Statement statement = connection.createStatement();
            String query = "INSERT INTO Sqills.Booking(bookingdate, starttime, endtime, roomID) VALUES ('2030-12-12', '9:00:00', '10:00:00', 1)";
            int updatedRows = statement.executeUpdate(query);
            statement.close();
            connection.close();
            System.out.println("Inserted " + updatedRows + " row(s)");
        } catch(SQLException sqle) {
            System.err.println(sqle);
        }
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
    @Test
    public void removeFromDatabase(){
        Connection connection = DatabaseConnectionFactory.getConnection();
        try {
            Statement statement = connection.createStatement();
            String query = "DELETE FROM sqills.Booking WHERE bookingdate = '2030-12-12'";
            int updatedRows = statement.executeUpdate(query);
            statement.close();
            connection.close();
            System.out.println("Deleted " + updatedRows + " row(s)");
        } catch(SQLException sqle) {
            System.err.println(sqle);
        }
    }
}
