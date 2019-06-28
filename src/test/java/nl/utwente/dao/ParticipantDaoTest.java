package nl.utwente.dao;

import nl.utwente.db.DatabaseConnectionFactory;
import nl.utwente.exceptions.*;
import org.junit.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Time;

import static junit.framework.TestCase.fail;
import static nl.utwente.dao.BookingDao.insertBookingToday;
import static nl.utwente.dao.ParticipantDao.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParticipantDaoTest {
    private static Connection connection;
    private static String bookingTitle = "Test booking title";
    private static String email = "sqills_tablet@gmail.com";
    private static int roomID = -12120;
    private static String roomName = "test_room";
    private static Time end = new Time(23,59,59);
    private static int bookingID;
    private static String participantEmail = "pl.frolov99@gmail.com";

    @BeforeClass
    public static void setup() throws BookingException, DAOException {
        connection = DatabaseConnectionFactory.conn;
    }

    @AfterClass
    public static void shutDown() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Before
    public void beforeEachTest() throws BookingException, DAOException, SQLException {
        // This structure is not perfect. JUnit requires @Before methods to be non-static
        // But we need createRoom to be static so that we can easily access it from BookingDaoTest
        // This was the best solution
        createRoom();
        makeBooking(new Time(23,59,58));
    }

    private int makeBooking(Time startTime) throws BookingException, DAOException, SQLException {
        Time endTime = new Time(23, 59, 59);
        connection.commit();
        bookingID = insertBookingToday(roomName, startTime, endTime, email, false, bookingTitle);
        return bookingID;
    }

    public static void createRoom() {
        String query = "INSERT INTO sqills.room (room_id, room_name) VALUES (" + roomID + ", '" + roomName + "')";
        try {
            connection.createStatement().execute(query);
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    @Test
    public void addParticipants() throws DAOException, InvalidBookingIDException, InvalidEmailException, InvalidUserIDException {
        assertEquals(getParticipantsOfBooking(bookingID).size(), 0);
        addParticipantEmailToBooking(bookingID, participantEmail);
        assertEquals(getParticipantsOfBooking(bookingID).get(0).getEmail(), "pl.frolov99@gmail.com");
        assertEquals(getParticipantsOfBooking(bookingID).size(), 1);
        removeParticipant(bookingID, 10000);
        assertEquals(getParticipantsOfBooking(bookingID).size(), 1);
        //3 is id of pl.frolov99@gmail.com
        removeParticipant(bookingID, 3);
        assertEquals(getParticipantsOfBooking(bookingID).size(), 1);
    }

    @After
    public void afterEachTest() {
        try {
//            deleteBooking(bookingID);
//            deleteRoom();
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
//        catch (DAOException e) {
//            e.printStackTrace();
//        } catch (InvalidBookingIDException e) {
//            e.printStackTrace();
//        }
    }

    private void deleteRoom() throws SQLException {
        String query = "DELETE FROM sqills.room WHERE room_id = " + roomID;
        connection.createStatement().execute(query);
    }
}
