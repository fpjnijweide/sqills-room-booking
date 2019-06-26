package nl.utwente.dao;

import nl.utwente.db.DatabaseConnectionFactory;
import nl.utwente.exceptions.DAOException;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.fail;
import static nl.utwente.dao.RoomDao.isValidRoomID;
import static org.junit.Assert.assertFalse;

public class RoomDaoTest {
    @Test
    public void testIsValidBookingNotValid() {
        try {
            assertFalse(isValidRoomID(-2));
        } catch (DAOException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testIsValidBookingValid() {
        try {
            Connection connection = DatabaseConnectionFactory.conn;
            String insertQuery = "INSERT INTO sqills.room (roomid) VALUES (100);";
            connection.createStatement().execute(insertQuery);

            boolean isValid = isValidRoomID(100);

            String deleteQuery = "DELETE FROM sqills.room WHERE roomid = 100";
            connection.createStatement().execute(deleteQuery);

            assertTrue(isValid);
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            fail();
        } catch (DAOException e) {
            e.printStackTrace();
            fail();
        }
    }
}
