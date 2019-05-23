package nl.utwente.resource;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import nl.utwente.dao.BookingDao;
import nl.utwente.dao.RoomDao;
import nl.utwente.model.Booking;
import nl.utwente.model.SpecifiedBooking;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.Time;
import java.util.List;

@Path("/room")
public class RoomResource {
    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    /**
     * Returns all of today's bookings for a specific room.
     * @param roomID Number specifying the room
     * @return JSON object containing all of today's bookings for a specific room
     */
    public List<Integer> getRoomList () {
        return RoomDao.getAllRoomsIDs();
    }

    @GET
    @Path("/{roomID}")
    @Produces(MediaType.APPLICATION_JSON)
    /**
     * Returns all of today's bookings for a specific room.
     * @param roomID Number specifying the room
     * @return JSON object containing all of today's bookings for a specific room
     */
    public List<SpecifiedBooking> getBookingsForSpecificRoomToday (
        @PathParam("roomID") Integer roomID
    ) {
        return BookingDao.getBookingsForRoomToday(roomID);
    }


    @POST
    @Path("/{roomID}/book")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    /**
     * Create a booking for a specific room for today.
     * @param roomID specifies the room
     * @param specifiedBooking startTime and endTime specifying the times of the booking
     * @return JSON object containing a "success" boolean value
     */
    public String createBookingForSpecificRoom (
        @PathParam("roomID") Integer roomID,
        Booking booking
    ) {
        Time startTime = booking.getStartTime();
        Time endTime = booking.getEndTime();
        boolean valid = RoomDao.isValidRoomID(roomID) &&
            BookingDao.isValidBookingToday(roomID, startTime, endTime);

        if (valid) {
            BookingDao.insertBookingToday(roomID, startTime, endTime, booking.getEmail(), booking.isPrivate());
        }

        final JsonNodeFactory factory = JsonNodeFactory.instance;
        ObjectNode success = factory.objectNode();
        success.put("success", valid);
        return success.toString();
    }

    public RoomResource(){

    }
}
