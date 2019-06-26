package nl.utwente.resource;

import nl.utwente.dao.BookingDao;
import nl.utwente.dao.RoomDao;
import nl.utwente.exceptions.BookingException;
import nl.utwente.exceptions.DAOException;
import nl.utwente.exceptions.InvalidRoomNameException;
import nl.utwente.model.Booking;
import nl.utwente.model.OutputBooking;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.sql.Time;
import java.util.List;

import static nl.utwente.exceptions.ExceptionHandling.*;

@Path("/room")
public class RoomResource {
    @Context
    SecurityContext securityContext;

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
        try {
            return RoomDao.getAllRoomNames();
        } catch (DAOException e) {
            throw500("Something went terribly wrong");
        }
        return null;
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
        String userEmail = null;
        if (securityContext.getUserPrincipal()!=null) {
            userEmail = securityContext.getUserPrincipal().getName();
        }
        try {
            return BookingDao.getBookingsForRoomToday(roomName,userEmail);
        } catch (InvalidRoomNameException e) {
            throw404(e.getMessage());
        } catch (DAOException e) {
            throw500("Something went terribly wrong");
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
        try {
            return BookingDao.insertBookingToday(roomName, booking.getStartTime(), booking.getEndTime(),
                    booking.getEmail(), booking.getIsPrivate(), booking.getTitle());
        } catch (BookingException e) {
            throw400(e.getMessage());
        } catch (DAOException e) {
            throw500("Something went terribly wrong");
        }
        return 0;
    }

    @GET
    @Path("/available")
    @Produces(MediaType.APPLICATION_JSON)
    /**
     * Returns a list of all currently available rooms.
     */
    public List<String> getAvailableRooms() {
        try {
            return RoomDao.getCurrentlyAvailableRooms();
        } catch (DAOException e) {
            throw500("Something went terribly wrong");
        }
        return null;
    }

    @GET
    @Path("/{roomName}/availableUntil")
    @Produces(MediaType.APPLICATION_JSON)
    public Time getAvailableUntil(@PathParam("roomName") String roomName) {
        try {
            return RoomDao.getFreeUntil(roomName);
        } catch (InvalidRoomNameException e) {
            throw404(e.getMessage());
        } catch (DAOException e) {
            throw500("Something went terribly wrong");
        }
        return null;
    }

    @GET
    @Path("/{roomName}/week")
    @Produces(MediaType.APPLICATION_JSON)
    public List<OutputBooking> getBookingsForThisWeek(@PathParam("roomName") String roomName) {
        String userEmail = null;
        if (securityContext.getUserPrincipal()!=null) {
            userEmail = securityContext.getUserPrincipal().getName();
        }
        try {
            return RoomDao.getBookingsForThisWeek(roomName,userEmail);
        } catch (InvalidRoomNameException e) {
            throw404(e.getMessage());
        } catch (DAOException e) {
            throw500("Something went terribly wrong");
        }

        return null;
    }
}
