package nl.utwente.api.room;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import nl.utwente.model.BookingModel;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import java.sql.Time;
import java.util.List;

@Path("/room")
public class Room {
    @GET
    @Path("/{roomNumber}")
    @Produces(MediaType.APPLICATION_JSON)
    /**
     * Returns all of today's bookings for a specific room.
     * @param roomNumber Number specifying the room
     * @return JSON object containing all of today's bookings for a specific room
     */
    public String getBookingsForSpecificRoomToday (
        @PathParam("roomNumber") Integer roomNumber,
        @Context UriInfo uriInfo
    ) {
        final JsonNodeFactory factory = JsonNodeFactory.instance;
        ArrayNode bookingsNode = factory.arrayNode();

        List<BookingModel> bookingModels = BookingModel.getBookingsForRoomToday(roomNumber);

        for (BookingModel bookingModel : bookingModels) {
            bookingsNode.add(bookingModel.toJSONNode());
        }

        return bookingsNode.toString();
    }

    @POST
    @Path("/{roomNumber}/create")
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
        TimeSlot timeSlot,
        @Context UriInfo uriInfo
    ) {
        Time startTime = Time.valueOf(timeSlot.getStartTime());
        Time endTime = Time.valueOf(timeSlot.getEndTime());
        boolean valid = nl.utwente.model.Room.isValidRoomID(roomNumber) &&
            BookingModel.isValidBookingToday(roomNumber, timeSlot.getStartTime(), timeSlot.getEndTime());

        if (valid) {
            BookingModel.insertBookingToday(roomNumber, startTime, endTime);
        }

        final JsonNodeFactory factory = JsonNodeFactory.instance;
        ObjectNode success = factory.objectNode();
        success.put("success", valid);
        return success.toString();
    }
}
