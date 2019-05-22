package nl.utwente.resource;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import nl.utwente.dao.BookingDao;
import nl.utwente.model.Booking;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/booking")
public class BookingResource {
    /**
     * Returns a specific booking from the database.
     * @param bookingID ID of booking to be returned
     * @return JSON Object for the requested booking
     */

    public BookingResource(){

    }
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{bookingID}")
    public Booking getSpecificBooking(@PathParam("bookingID") int bookingID) {
        return BookingDao.getSpecificBooking(bookingID);
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
    public String createBooking(Booking booking) {

        System.out.println(booking.toString());

        boolean result = BookingDao.createBooking(booking);

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
        Booking booking
    ) {
        boolean result = BookingDao.updateBooking(bookingID, booking);

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
        boolean success = BookingDao.deleteBooking(bookingID);
        final JsonNodeFactory factory = JsonNodeFactory.instance;
        ObjectNode node = factory.objectNode();
        node.put("success", success);
        return node.toString();
    }
}
