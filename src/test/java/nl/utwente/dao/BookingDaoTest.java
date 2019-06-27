package nl.utwente.dao;

import nl.utwente.db.DatabaseConnectionFactory;
import nl.utwente.exceptions.*;
import nl.utwente.model.OutputBooking;
import nl.utwente.model.SpecifiedBooking;
import org.junit.*;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static junit.framework.TestCase.fail;
import static nl.utwente.dao.BookingDao.*;
import static nl.utwente.dao.DaoTestUtils.*;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BookingDaoTest {


    private Date date = Date.valueOf("2030-12-12");
    private Time startTime = Time.valueOf("9:00:00");
    private Time endTime = Time.valueOf("10:00:00");
    private int bookingID = -1;

    SpecifiedBooking createdBooking = new SpecifiedBooking(startTime,
        endTime, roomName, date, email,
        false, bookingTitle);

    //booking for same time, date room: should not be accepted
    SpecifiedBooking OverlappingBooking = new SpecifiedBooking(Time.valueOf("9:00:00"),
        Time.valueOf("10:00:00"), roomName, Date.valueOf("2030-12-12"), email,
        false, bookingTitle);

    //valid booking with startime just after endtime current booking: should be accepted
    SpecifiedBooking bookingFrom10To11 = new SpecifiedBooking(Time.valueOf("10:00:00"),
        Time.valueOf("11:00:00"), roomName, Date.valueOf("2030-12-12"), email,
        false, bookingTitle);

    //booking with the current booking in between: should not be accepted
    SpecifiedBooking overlappingBooking2 = new SpecifiedBooking(Time.valueOf("9:00:00"),
        Time.valueOf("11:00:00"), roomName, Date.valueOf("2030-12-12"), email,
        false, bookingTitle);

    //other date same time same room: should be accepted
    SpecifiedBooking booking4 = new SpecifiedBooking(Time.valueOf("9:00:00"),
        Time.valueOf("11:00:00"), roomName, Date.valueOf("2030-12-13"), email,
        false, bookingTitle);

    //other room booking for same time: should be accepted
    SpecifiedBooking otherRoomBooking = new SpecifiedBooking(Time.valueOf("9:00:00"),
        Time.valueOf("11:00:00"), "Test room 2", Date.valueOf("2030-12-12"), email,
        false, bookingTitle);

    //endtime before startime: should not be accepted
    SpecifiedBooking endTimeBeforeStartTimeBooking = new SpecifiedBooking(Time.valueOf("13:00:00"),
        Time.valueOf("12:00:00"), roomName, Date.valueOf("2030-12-12"), email,
        false, bookingTitle);

    //booking with endtime just before current booking starttime: should be accepted
    SpecifiedBooking earlyBooking = new SpecifiedBooking(Time.valueOf("8:00:00"),
        Time.valueOf("9:00:00"), roomName, Date.valueOf("2030-12-12"), email,
        false, bookingTitle);

//    private Time startTime = currentTimePlusMinutes(2);
//    private Time endTime = new Time(23, 59, 00);

//    private LocalDate localDate = LocalDate.now();

//    private Date date = new Date(localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth());
//    private SpecifiedBooking createdBooking = new SpecifiedBooking(startTime,endTime,roomName, date, email,false,bookingTitle);

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
        createRoom(roomID, roomName);
        try {
            bookingID = createBooking(createdBooking);
        } catch (BookingException | DAOException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    @Test
    public void testGetOutputBooking() {
        try {
            OutputBooking booking = getOutputBooking(bookingID);
            assertEquals(booking.getDate(), date);
            assertEquals(booking.getStartTime(), startTime);
            assertEquals(booking.getEndTime(), endTime);
            assertEquals(booking.getRoomName(), roomName);
            assertEquals(booking.getTitle(), bookingTitle);
            assertEquals(booking.getBookingid(), bookingID);
            assertEquals(booking.getUserName(), UserDao.getUserFromEmail(email).getName());
        } catch (InvalidEmailException | DAOException | InvalidBookingIDException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    @Test
    public void testGetSpecificBookingNonExistingBooking() {
        AtomicReference<OutputBooking> booking = null;
        assertThrows(InvalidBookingIDException.class, () -> {
            booking.set(getOutputBooking(-1));
        });
        assertNull(booking);
    }

    @Test
    public void testDeleteBookingNonExisting() {
        assertThrows(InvalidBookingIDException.class, () -> {
            deleteBooking(-1);
        });
    }

    @Test
    public void testValidBooking() {
        createRoom(-101, "Test room 2");

        try {
            boolean result1 = isValidBooking(OverlappingBooking);
            boolean result2 = isValidBooking(bookingFrom10To11);
            boolean result3 = isValidBooking(overlappingBooking2);
            boolean result4 = isValidBooking(booking4);
            boolean result5 = isValidBooking(otherRoomBooking);
            boolean result6 = isValidBooking(endTimeBeforeStartTimeBooking);
            boolean result7 = isValidBooking(earlyBooking);
            assertFalse(result1);
            assertTrue(result2);
            assertFalse(result3);
            assertTrue(result4);
            assertTrue(result5);
            assertFalse(result6);
            assertTrue(result7);
        } catch (DAOException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }


        try {
            deleteRoom(-101);
        } catch (SQLException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }


    @Test
    public void testCreateBookingInvalidBooking() {
        // Creates a duplicate booking
        assertThrows(BookingException.class, () -> {
           createBooking(createdBooking);
        });
    }

    @Test
    public void testUpdateBooking() {
        assertDoesNotThrow(() -> {
            updateBooking(bookingID, createdBooking);
        });

        assertThrows(BookingException.class, () -> {
            updateBooking(bookingID, endTimeBeforeStartTimeBooking);
        });

        assertThrows(InvalidBookingIDException.class, () -> {
            updateBooking(-100, earlyBooking);
        });

    }

    @Test
    public void testGetBookingsForRoomToday() {
        List<OutputBooking> bookings = null;
        try {
            // TODO the booking I made is not acutally today lmao
            bookings = BookingDao.getBookingsForRoomToday(roomName);
        } catch (InvalidRoomNameException | DAOException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

        assertEquals(bookings.size(), 1);

        for (OutputBooking booking : bookings) {
            try {
                assertEquals(booking.getUserName(), UserDao.getUserFromEmail(email).getName());
            } catch (InvalidEmailException | DAOException e) {
                e.printStackTrace();
                fail(e.getMessage());
            }
            assertEquals(booking.getBookingid(), bookingID);
            assertEquals(booking.getTitle(), bookingTitle);
            assertEquals(booking.getRoomName(), roomName);
            assertEquals(booking.getDate(), date);
        }

        try {
            deleteBooking(bookingID);
            bookings = BookingDao.getBookingsForRoomToday(roomName);
        } catch (InvalidRoomNameException | DAOException | InvalidBookingIDException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
        assertEquals(bookings.size(), 0);

        try {
            createBooking(createdBooking);
        } catch (BookingException | DAOException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

//    @Test
//    public void testInsertBookingToday() {
//        try {
//            Time startTime = Time.valueOf("01:00:00");
//            Time endTime = Time.valueOf("02:00:00");
//
//            BookingDao.insertBookingToday(-1, startTime, endTime, "antoniaheath@gmail.com", false);
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
//            BookingDao.insertBookingToday(-1, startTime0, endTime0, "antoniaheath@gmail.com", false);
//
//            Time startTime = Time.valueOf("01:00:00");
//            Time endTime = Time.valueOf("02:00:00");
//            BookingDao.insertBookingToday(-1, startTime, endTime, "antoniaheath@gmail.com", false);
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

    @After
    public void afterEachTest() {
        try {
            connection.commit();
            deleteBooking(bookingID);
            deleteRoom(roomID);
        } catch (SQLException | InvalidBookingIDException | DAOException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }
}
