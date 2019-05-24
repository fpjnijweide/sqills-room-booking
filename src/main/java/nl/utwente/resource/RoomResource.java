package nl.utwente.resource;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import nl.utwente.dao.BookingDao;
import nl.utwente.dao.RoomDao;
import nl.utwente.model.Booking;
import nl.utwente.model.TimeSlot;

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
     * @param roomNumber Number specifying the room
     * @return JSON object containing all of today's bookings for a specific room
     */
    public List<String> getRoomList () {
        return RoomDao.getAllRoomsIDs();
    }

    @GET
    @Path("/{roomNumber}")
    @Produces(MediaType.APPLICATION_JSON)
    /**
     * Returns all of today's bookings for a specific room.
     * @param roomNumber Number specifying the room
     * @return JSON object containing all of today's bookings for a specific room
     */
    public List<Booking> getBookingsForSpecificRoomToday (
        @PathParam("roomNumber") Integer roomNumber
    ) {
        return BookingDao.getBookingsForRoomToday(roomNumber);
    }


    @POST
    @Path("/{roomNumber}/book")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    /**
     * Create a booking for a specific room for today.
     * @param roomNumber specifies the room
     * @param timeSlot startTime and endTime specifying the times of the booking
     * @return JSON object containing a "success" boolean value
     */
    public String createBookingForSpecificRoom (
        @PathParam("roomNumber") Integer roomNumber,
        TimeSlot timeSlot
    ) {
        Time startTime = Time.valueOf(timeSlot.getStartTime());
        Time endTime = Time.valueOf(timeSlot.getEndTime());
        boolean valid = RoomDao.isValidRoomID(roomNumber) &&
            BookingDao.isValidBookingToday(roomNumber, timeSlot.getStartTime(), timeSlot.getEndTime());

        if (valid) {
            BookingDao.insertBookingToday(roomNumber, startTime, endTime);
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
    @Path("/{roomID}/availableUntil")
    @Produces(MediaType.APPLICATION_JSON)
    public Time getAvailableUntil(@PathParam("roomID") int roomID) {
        System.out.println(RoomDao.getFreeUntil(roomID));
        return RoomDao.getFreeUntil(roomID);
    }

    @GET
    @Path("/{roomID}/week")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Booking> getBookingsForThisWeek(@PathParam("roomID") int roomID) {
        return RoomDao.getBookingsForThisWeek(roomID);
    }
}
