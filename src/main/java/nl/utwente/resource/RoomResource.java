package nl.utwente.resource;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import nl.utwente.dao.BookingDao;
import nl.utwente.dao.RoomDao;
import nl.utwente.model.Booking;
import nl.utwente.model.OutputBooking;
import nl.utwente.model.SpecifiedBooking;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.Time;
import java.util.List;

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
        return BookingDao.getBookingsForRoomToday(roomName);
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
    public String createBookingForSpecificRoom (
        @PathParam("roomName") String roomName,
        Booking booking
    ) {
        Time startTime = booking.getStartTime();
        Time endTime = booking.getEndTime();
        boolean valid = RoomDao.isValidRoomName(roomName) &&
            BookingDao.isValidBookingToday(roomName, startTime, endTime);

        if (valid) {
            BookingDao.insertBookingToday(roomName, startTime, endTime, booking.getEmail(), booking.getIsPrivate());
        }

        final JsonNodeFactory factory = JsonNodeFactory.instance;
        ObjectNode success = factory.objectNode();
        success.put("success", valid);
        return success.toString();
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
        System.out.println(RoomDao.getFreeUntil(roomName));
        return RoomDao.getFreeUntil(roomName);
    }

    @GET
    @Path("/{roomName}/week")
    @Produces(MediaType.APPLICATION_JSON)
    public List<OutputBooking> getBookingsForThisWeek(@PathParam("roomName") String roomName) {
        return RoomDao.getBookingsForThisWeek(roomName);
    }
}
