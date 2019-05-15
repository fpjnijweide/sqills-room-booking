package nl.utwente.api;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import nl.utwente.model.BookingModel;

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
    @Path("/{bookingID}")
    public String getSpecificBooking(@PathParam("bookingID") int bookingID) {
        BookingModel booking = BookingModel.getSpecificBooking(bookingID);
        return booking.toJSONNode().toString();
    }

    /**
     * Creates a booking.
     * @return JSON object containing a "success" boolean field specifying whether the booking was
     *         successfully created
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/create")
    // Todo: Add validity check
    public String createBooking(BookingBean bookingBean) {
        boolean result = BookingModel.createBooking(
            bookingBean.getStartTime(),
            bookingBean.getEndTime(),
            bookingBean.getDate(),
            bookingBean.getRoomNumber()
        );

        final JsonNodeFactory factory = JsonNodeFactory.instance;
        ObjectNode success = factory.objectNode();
        success.put("success", result);
        return success.toString();
    }

    /**
     * Updates a specified booking.
     * @param bookingID ID specifying the booking to be updated
     * @return JSON object containing a "success" boolean field specifying whether the booking was
     *         successfully updated
     */
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{bookingID}/update")
    // Todo: Add validity check
    public String createBooking(
        @PathParam("bookingID") int bookingID,
        BookingBean bookingBean
    ) {
        boolean result = BookingModel.updateBooking(
            bookingID,
            bookingBean.getStartTime(),
            bookingBean.getEndTime(),
            bookingBean.getDate(),
            bookingBean.getRoomNumber()
        );

        final JsonNodeFactory factory = JsonNodeFactory.instance;
        ObjectNode success = factory.objectNode();
        success.put("success", result);
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
    public String createBooking(@PathParam("bookingID") int bookingID) {
        boolean success = BookingModel.deleteBooking(bookingID);
        final JsonNodeFactory factory = JsonNodeFactory.instance;
        ObjectNode node = factory.objectNode();
        node.put("success", success);
        return node.toString();
    }
}
