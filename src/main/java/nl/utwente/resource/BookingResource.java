package nl.utwente.resource;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import nl.utwente.dao.BookingDao;
import nl.utwente.dao.ParticipantDao;
import nl.utwente.model.OutputBooking;
import nl.utwente.model.SpecifiedBooking;
import nl.utwente.model.User;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/booking")
public class BookingResource {
    /**
     * Returns a specific booking from the database.
     * @param bookingID ID of booking to be returned
     * @return JSON Object for the requested booking
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{bookingID}")
    public OutputBooking getSpecificBooking(@PathParam("bookingID") int bookingID) {
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
    public String createBooking(SpecifiedBooking booking) {
        int result = BookingDao.createBooking(booking);

        final JsonNodeFactory factory = JsonNodeFactory.instance;
        ObjectNode success = factory.objectNode();
        success.put("bookingid", result);
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
    public String updateBooking(
        @PathParam("bookingID") int bookingID,
        SpecifiedBooking booking
    ) {
        boolean result = BookingDao.updateBooking(bookingID, booking);

        final JsonNodeFactory factory = JsonNodeFactory.instance;
        ObjectNode success = factory.objectNode();
        success.put("success", result);
        return success.toString();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{bookingID}/participants")
    public List<User> getParticipants(@PathParam("bookingID") int bookingID) {
        return ParticipantDao.getParticipantIDsOfBooking(bookingID);
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
    public String deleteBooking(@PathParam("bookingID") int bookingID) {
        boolean success = BookingDao.deleteBooking(bookingID);
        final JsonNodeFactory factory = JsonNodeFactory.instance;
        ObjectNode node = factory.objectNode();
        node.put("success", success);
        return node.toString();
    }
}
