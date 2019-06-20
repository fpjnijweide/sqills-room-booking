package nl.utwente.resource;

import nl.utwente.dao.BookingDao;
import nl.utwente.dao.RoomDao;
import nl.utwente.exceptions.BookingException;
import nl.utwente.exceptions.InvalidRoomNameException;
import nl.utwente.model.Booking;
import nl.utwente.model.OutputBooking;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.Time;
import java.util.List;

import static nl.utwente.exceptions.ExceptionHandling.throw400;
import static nl.utwente.exceptions.ExceptionHandling.throw404;

@Path("/room")
public class RoomResource {
    public RoomResource(){ }


    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    /**
     * Returns all of today's bookings for a specific room.
     * @param roomID Number specifying the room
     * @return JSON object containing all of today's bookings for a specific room
     */
    public List<String> getRoomList () {
        return RoomDao.getAllRoomNames();
    }

    @GET
    @Path("/{roomName}")
    @Produces(MediaType.APPLICATION_JSON)
    /**
     * Returns all of today's bookings for a specific room.
     * @param roomID Number specifying the room
     * @return JSON object containing all of today's bookings for a specific room
     */
    public List<OutputBooking> getBookingsForSpecificRoomToday (
        @PathParam("roomName") String roomName
    ) {
        try {
            return BookingDao.getBookingsForRoomToday(roomName);
        } catch (InvalidRoomNameException e) {
            throw404(e);
        }

        return null;
    }

    @POST
    @Path("/{roomName}/book")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    /**
     * Create a booking for a specific room for today.
     * @param roomID specifies the room
     * @param specifiedBooking startTime and endTime specifying the times of the booking
     * @return JSON object containing a "success" boolean value
     */
    public int createBookingForSpecificRoom (
        @PathParam("roomName") String roomName,
        @Valid Booking booking
    ) {
        int roomID = 0;
        try {
            roomID = BookingDao.insertBookingToday(roomName, booking.getStartTime(), booking.getEndTime(),
                    booking.getEmail(), booking.getIsPrivate(), booking.getTitle());
        } catch (BookingException e) {
            throw400(e);
        }

        if (roomID != -1 && roomID != 0) {
            return roomID;
        } else {
            throw new InternalServerErrorException("Something went wrong in createBookingForSpecificRoom");
        }
    }

    @GET
    @Path("/available")
    @Produces(MediaType.APPLICATION_JSON)
    /**
     * Returns a list of all currently available rooms.
     */
    public List<String> getAvailableRooms() {
        return RoomDao.getCurrentlyAvailableRooms();
    }

    @GET
    @Path("/{roomName}/availableUntil")
    @Produces(MediaType.APPLICATION_JSON)
    public Time getAvailableUntil(@PathParam("roomName") String roomName) {
        try {
            return RoomDao.getFreeUntil(roomName);
        } catch (InvalidRoomNameException e) {
            throw404(e);
        }
        return null;
    }

    @GET
    @Path("/{roomName}/week")
    @Produces(MediaType.APPLICATION_JSON)
    public List<OutputBooking> getBookingsForThisWeek(@PathParam("roomName") String roomName) {
        try {
            return RoomDao.getBookingsForThisWeek(roomName);
        } catch (InvalidRoomNameException e) {
            throw404(e);
        }

        return null;
    }
}
