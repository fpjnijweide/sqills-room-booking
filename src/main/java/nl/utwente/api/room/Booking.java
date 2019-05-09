package nl.utwente.api.room;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/booking")
public class Booking {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{bookingID}")
    public String getSpecificBooking(@PathParam("bookingID") int bookingID) {
        final JsonNodeFactory factory = JsonNodeFactory.instance;
        ObjectNode booking = factory.objectNode();
        booking.put("roomNumber", 10);
        booking.put("startTime", "12:00");
        booking.put("endTime", "13:00");
        booking.put("date", "01/31/2019");
        booking.put("bookingID", bookingID);
        return booking.toString();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/create")
    public String createBooking(
        @QueryParam("roomNumber") int roomNumber,
        @QueryParam("date") String date,
        @QueryParam("startTime") String startTime,
        @QueryParam("endTime") String endTime
    ) {
        final JsonNodeFactory factory = JsonNodeFactory.instance;
        ObjectNode success = factory.objectNode();
        success.put("success", false);
        return success.toString();
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{bookingID}/update")
    public String createBooking(
        @PathParam("bookingID") int bookingID,
        @QueryParam("roomNumber") int roomNumber,
        @QueryParam("date") String date,
        @QueryParam("startTime") String startTime,
        @QueryParam("endTime") String endTime
    ) {
        final JsonNodeFactory factory = JsonNodeFactory.instance;
        ObjectNode success = factory.objectNode();
        success.put("success", false);
        return success.toString();
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{bookingID}/delete")
    public String createBooking(
        @PathParam("bookingID") int bookingID
    ) {
        final JsonNodeFactory factory = JsonNodeFactory.instance;
        ObjectNode success = factory.objectNode();
        success.put("success", false);
        return success.toString();
    }
}
