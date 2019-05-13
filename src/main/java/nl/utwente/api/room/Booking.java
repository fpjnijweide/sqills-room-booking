package nl.utwente.api.room;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/booking")
public class Booking {
    /**
     * Returns a specific booking from the database.
     * @param bookingID ID of booking to be returned
     * @return JSON Object for the requested booking
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{bookingID}")
    // Todo: Implement
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

    /**
     * Creates a booking.
     * @param roomNumber number of room in which the meeting will take place
     * @param date date of booking
     * @param startTime start time of booking
     * @param endTime end time of booking
     * @return JSON object containing a "success" boolean field specifying whether the booking was
     *         successfully created
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/create")
    // Todo: Implement
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

    /**
     * Updates a specified booking.
     * @param bookingID ID specifying the booking to be updated
     * @param roomNumber new roomNumber of the booking
     * @param date new date of the booking
     * @param startTime new start time of the booking
     * @param endTime new end time of the booking
     * @return JSON object containing a "success" boolean field specifying whether the booking was
     *         successfully updated
     */
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{bookingID}/update")
    // Todo: Implement
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

    /**
     * Deletes a specific booking
     * @param bookingID Path parameter specifying the booking to be deleted
     * @return JSON object containing a "success" boolean field specifying whether the booking was
     *         successfully deleted
     */
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{bookingID}/delete")
    // Todo: Implement
    public String createBooking(
        @PathParam("bookingID") int bookingID
    ) {
        final JsonNodeFactory factory = JsonNodeFactory.instance;
        ObjectNode success = factory.objectNode();
        success.put("success", false);
        return success.toString();
    }
}
