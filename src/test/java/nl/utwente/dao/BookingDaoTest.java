package nl.utwente.dao;

import nl.utwente.db.DatabaseConnectionFactory;
import nl.utwente.exceptions.*;
import nl.utwente.model.OutputBooking;
import nl.utwente.model.SpecifiedBooking;
import org.junit.*;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static junit.framework.TestCase.fail;
import static nl.utwente.dao.BookingDao.*;
import static nl.utwente.dao.DaoTestUtils.*;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BookingDaoTest {


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
    private Date date = Date.valueOf("2030-12-12");
    private Time startTime = Time.valueOf("9:00:00");
    private Time endTime = Time.valueOf("10:00:00");
    SpecifiedBooking createdBooking = new SpecifiedBooking(startTime,
        endTime, roomName, date, email,
        false, bookingTitle);
    private int bookingID = -1;

//    private Time startTime = currentTimePlusMinutes(2);
//    private Time endTime = new Time(23, 59, 00);

//    private LocalDate localDate = LocalDate.now();

//    private Date date = new Date(localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth());
//    private SpecifiedBooking createdBooking = new SpecifiedBooking(startTime,endTime,roomName, date, email,false,bookingTitle);

    @BeforeClass
    public static void setup() {
        try {
            connection = DatabaseConnectionFactory.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
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
            boolean result1 = isValidBooking(OverlappingBooking, connection);
            boolean result2 = isValidBooking(bookingFrom10To11, connection);
            boolean result3 = isValidBooking(overlappingBooking2, connection);
            boolean result4 = isValidBooking(booking4, connection);
            boolean result5 = isValidBooking(otherRoomBooking, connection);
            boolean result6 = isValidBooking(endTimeBeforeStartTimeBooking, connection);
            boolean result7 = isValidBooking(earlyBooking, connection);
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
        // TODO Marten fixed this on his branch
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

        assertEquals(bookings.size(), 0);
        AtomicInteger tempID = new AtomicInteger();
        assertDoesNotThrow(() -> {
            tempID.set(insertBookingToday(roomName, startTime, endTime, email, false, bookingTitle));
        });

        try {
            bookings = BookingDao.getBookingsForRoomToday(roomName);
        } catch (InvalidRoomNameException | DAOException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }


        for (OutputBooking booking : bookings) {
            try {
                assertEquals(booking.getUserName(), UserDao.getUserFromEmail(email).getName());
            } catch (InvalidEmailException | DAOException e) {
                e.printStackTrace();
                fail(e.getMessage());
            }
            assertEquals(booking.getBookingid(), tempID.intValue());
            assertEquals(booking.getTitle(), bookingTitle);
            assertEquals(booking.getRoomName(), roomName);
            Calendar currentTime = Calendar.getInstance();
            Date sqlDate = new Date((currentTime.getTime()).getTime());
            assertEquals(booking.getDate(), sqlDate); // TODO ASSERT TODAY
        }

        try {
            deleteBooking(tempID.intValue());
            bookings = BookingDao.getBookingsForRoomToday(roomName);
        } catch (InvalidRoomNameException | DAOException | InvalidBookingIDException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
        assertEquals(bookings.size(), 0);

    }

    @Test
    public void testInsertBookingToday() {
        try {
            int tempID = BookingDao.insertBookingToday(roomName, startTime, endTime, email, false, bookingTitle);
            List<OutputBooking> bookings = BookingDao.getBookingsForRoomToday(roomName);
            assertEquals(bookings.get(0).getStartTime(), startTime);
            assertEquals(bookings.get(0).getEndTime(), endTime);
            deleteBooking(tempID);
        } catch (BookingException | DAOException | InvalidRoomNameException | InvalidBookingIDException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    @Test
    public void testInsertBookingTodayInvalidBooking() {
        try {
            Time startTime1 = Time.valueOf("00:30:00");
            Time endTime1 = Time.valueOf("01:30:00");
            int tempID1 = BookingDao.insertBookingToday(roomName, startTime1, endTime1, email, false, bookingTitle);


            Time startTime2 = Time.valueOf("01:00:00");
            Time endTime2 = Time.valueOf("02:00:00");

            assertThrows(BookingException.class, () -> {
                int tempID2 = BookingDao.insertBookingToday(roomName, startTime2, endTime2, email, false, bookingTitle);
            });

            List<OutputBooking> bookings = BookingDao.getBookingsForRoomToday(roomName);
            assertEquals(bookings.get(0).getStartTime(), startTime1);
            assertEquals(bookings.get(0).getEndTime(), endTime1);
            assertEquals(bookings.size(), 1);

            deleteBooking(tempID1);
        } catch (InvalidBookingIDException | DAOException | BookingException | InvalidRoomNameException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    // TODO rest of methods

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
