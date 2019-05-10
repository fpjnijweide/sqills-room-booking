package nl.utwente.api.room;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import nl.utwente.model.Booking;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import java.util.List;

@Path("/room")
public class Room {
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String sayHi() {
        return "hi";
    }

    @GET
    @Path("/{roomNumber}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getBookingsForSpecificRoom (
        @PathParam("roomNumber") Integer roomNumber,
        @Context UriInfo uriInfo
    ) {
        final JsonNodeFactory factory = JsonNodeFactory.instance;
        ArrayNode bookingsNode = factory.arrayNode();

        List<Booking> bookings = Booking.getBookingsForRoomToday(roomNumber);

        for (Booking booking : bookings) {
            bookingsNode.add(booking.toJSONNode());
        }

        return bookingsNode.toString();
    }

    @POST
    @Path("/{roomNumber}/create")
    @Produces(MediaType.APPLICATION_JSON)
    public String createBookingForSpecificRoom (
        @PathParam("roomNumber") Integer roomNumber,
        @QueryParam("startTime") String startTime,
        @QueryParam("endTime") String endTime,
        @Context UriInfo uriInfo
    ) {
        System.out.println("Create booking at " + roomNumber + "from " + startTime + " to " + endTime + ".");
        final JsonNodeFactory factory = JsonNodeFactory.instance;
        ObjectNode success = factory.objectNode();
        success.put("success", true);
        return success.toString();
    }
}
