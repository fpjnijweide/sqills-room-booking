package nl.utwente.resource;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import nl.utwente.dao.BookingDao;
import nl.utwente.dao.ParticipantDao;
import nl.utwente.model.OutputBooking;
import nl.utwente.model.SpecifiedBooking;
import nl.utwente.model.User;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NoContentException;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Locale;

@Path("/booking")
public class BookingResource {
    @Context HttpServletResponse response;



    /**
     * Returns a specific booking from the database.
     * @param bookingID ID of booking to be returned
     * @return JSON Object for the requested booking
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{bookingID}")
    public OutputBooking getSpecificBooking(@PathParam("bookingID") int bookingID) {
        OutputBooking booking = BookingDao.getSpecificBooking(bookingID);
        if (booking!=null){
            return booking;
        } else {
            throw new NotFoundException();
        }

    }

    /**
     * Creates a booking.
     * @return JSON object containing a "success" boolean field specifying whether the booking was
     *         successfully created
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/create")
    public int createBooking(@Valid SpecifiedBooking booking) {
        int result = BookingDao.createBooking(booking);

        if (result!=-1){
            return result;
        } else {
            throw new BadRequestException();
        }
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
    public SpecifiedBooking updateBooking(
        @PathParam("bookingID") int bookingID,
        @Valid SpecifiedBooking booking
    ) {
        boolean result = BookingDao.updateBooking(bookingID, booking);

        if (result){
            return booking;
        } else {
            throw new BadRequestException();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{bookingID}/participants")
    public List<User> getParticipants(@PathParam("bookingID") int bookingID) {
        List<User> result = ParticipantDao.getParticipantsOfBooking(bookingID);
        if (result != null) {
            return result;
        } else {
            throw new NotFoundException();
        }
    }

    /**
     * Deletes a specific booking
     * @param bookingID Path parameter specifying the booking to be deleted
     * @return JSON object containing a "success" boolean field specifying whether the booking was
     *         successfully deleted
     */
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{bookingID}")
    public void deleteBooking(@PathParam("bookingID") int bookingID) {
        boolean success = BookingDao.deleteBooking(bookingID);
        if (!success) {
            throw new NotFoundException();
        }

    }
}
