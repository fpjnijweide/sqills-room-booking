package nl.utwente.dao;

import nl.utwente.db.DatabaseConnectionFactory;
import nl.utwente.exceptions.BookingException;
import nl.utwente.exceptions.DAOException;
import nl.utwente.exceptions.InvalidBookingIDException;
import nl.utwente.exceptions.InvalidRoomNameException;
import nl.utwente.model.OutputBooking;
import org.junit.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static junit.framework.TestCase.*;
import static nl.utwente.dao.BookingDao.deleteBooking;
import static nl.utwente.dao.BookingDao.insertBookingToday;
import static nl.utwente.dao.RoomDao.*;
import static org.junit.Assert.assertFalse;


public class RoomDaoTest {

    private static Connection connection;
    private static int roomID = -100;
    private static String roomName = "test_room";
    private static String bookingTitle = "Test booking title";
    private static String email = "sqills_tablet@gmail.com";

    @BeforeClass
    public static void setup() {
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
    public void beforeEachTest() {
        // This structure is not perfect. JUnit requires @Before methods to be non-static
        // But we need createRoom to be static so that we can easily access it from BookingDaoTest
        // This was the best solution
        createRoom();
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
    public void testThatInvalidRoomsAreInvalid() {
        try {
            assertFalse(isValidRoomID(-2));
            assertFalse(isValidRoomName("Some invalid room name"));
        } catch (DAOException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    @Test
    public void testThatValidRoomsAreValid() {
        try {
            assertTrue(isValidRoomID(roomID));
            assertTrue(isValidRoomName(roomName));
        } catch (DAOException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    @Test
    public void testGetAllRoomNames(){
        try {
            assertTrue(getAllRoomNames().contains(roomName));
            assertFalse(getAllRoomNames().contains("Invalid room name"));
        } catch (DAOException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    @Test
    public void testGetAllRoomIDs() {
        try {
            assertTrue(getAllRoomsIDs().contains(roomID));
            assertFalse(getAllRoomsIDs().contains(-200));
        } catch (DAOException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    @Test
    public void testThatAllRoomsMatchRoomIDs() {
        try {
            List<String> roomNames = getAllRoomNames();
            List<Integer> roomIDs = getAllRoomsIDs();
            for (int id : roomIDs) {
                assertEquals(id, getRoomID(getRoomName(id)));
            }
            for (String name : roomNames) {
                assertEquals(name, getRoomName(getRoomID(name)));
            }
        } catch (DAOException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    @Test
    public void testThatRoomIDMatchesRoomName() {
        try {
            assertEquals(roomID, getRoomID(roomName));
            assertEquals(roomName, getRoomName(roomID));
        } catch (DAOException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    @Test
    public void testThatRoomIsFree() {
        try {
            List<String> availableRooms = getCurrentlyAvailableRooms();
            assertTrue(availableRooms.contains(roomName));
        } catch (DAOException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    @Test
    public void testThatRoomIsNotFreeWhenBooked() {
        try {
            Time startTime = currentTimePlusMinutes(-2);
            int bookingId = makeBooking(startTime); // Creates booking from 2 minutes ago onwards
            List<String> availableRooms = getCurrentlyAvailableRooms();
            assertFalse(availableRooms.contains(roomName));
            deleteBooking(bookingId);
        } catch (DAOException | BookingException | InvalidBookingIDException | SQLException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    @Test
    public void testThatRoomIsAvailableAllDay() {
        try {
            assertNull(getFreeUntil(roomName));
        } catch (DAOException | InvalidRoomNameException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    @Test
    public void testThatRoomIsAvailableUntilBooking() {
        try {
            Time startTime = currentTimePlusMinutes(2);
            int bookingId = makeBooking(startTime); // Creates booking for 2 mins from now
            assertEquals(getFreeUntil(roomName),startTime);
            deleteBooking(bookingId);
        } catch (DAOException | BookingException | SQLException | InvalidBookingIDException | InvalidRoomNameException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    public void testBookingsForThisWeek() {
        try {
            List<OutputBooking> bookings = getBookingsForThisWeek(roomName);
            assertTrue(bookings.isEmpty());
            Time startTime = currentTimePlusMinutes(2);
            int bookingID = makeBooking(startTime);
            List<OutputBooking> bookings2 = getBookingsForThisWeek(roomName);
            assertEquals(1, bookings2.size());
            deleteBooking(bookingID);
        } catch (InvalidRoomNameException | DAOException | BookingException | SQLException | InvalidBookingIDException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    @After
    public void afterEachTest() {
        try {
            connection.commit();
            deleteRoom();
        } catch (SQLException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    private Time currentTimePlusMinutes(int offset) {
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MINUTE,offset);
        return new Time(cal.get(Calendar.HOUR_OF_DAY),cal.get(Calendar.MINUTE),0);
    }

    private int makeBooking(Time startTime) throws BookingException, DAOException, SQLException {
        Time endTime = new Time(23, 59, 59);
        connection.commit();
        return insertBookingToday(roomName, startTime, endTime, email, false, bookingTitle);
    }

    private void deleteRoom() throws SQLException {
        String query = "DELETE FROM sqills.room WHERE room_id = " + roomID;
        connection.createStatement().execute(query);
        connection.commit();
    }
}
