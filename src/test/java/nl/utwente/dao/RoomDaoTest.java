package nl.utwente.dao;

import nl.utwente.db.DatabaseConnectionFactory;
import nl.utwente.exceptions.BookingException;
import nl.utwente.exceptions.DAOException;
import nl.utwente.exceptions.InvalidBookingIDException;
import nl.utwente.exceptions.InvalidRoomNameException;
import nl.utwente.model.OutputBooking;
import org.junit.*;

import java.sql.SQLException;
import java.sql.Time;
import java.util.List;

import static junit.framework.TestCase.*;
import static nl.utwente.dao.BookingDao.deleteBooking;
import static nl.utwente.dao.BookingDao.insertBookingToday;
import static nl.utwente.dao.DaoTestUtils.*;
import static nl.utwente.dao.RoomDao.*;
import static org.junit.Assert.assertFalse;

public class RoomDaoTest {
    @BeforeClass
    public static void setup() {
        connection = DatabaseConnectionFactory.connection;
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
        createRoom(roomID,roomName);
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

    @Test
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
            deleteRoom(roomID);
        } catch (SQLException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    private int makeBooking(Time startTime) throws BookingException, DAOException, SQLException {
        Time endTime = new Time(23, 59, 00);
        connection.commit();
        return insertBookingToday(roomName, startTime, endTime, email, false, bookingTitle);
    }

}
