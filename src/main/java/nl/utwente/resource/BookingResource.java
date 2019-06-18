package nl.utwente.resource;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import nl.utwente.dao.BookingDao;
import nl.utwente.dao.ParticipantDao;
import nl.utwente.exceptions.BookingException;
import nl.utwente.exceptions.InvalidBookingIDException;
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

import static nl.utwente.exceptions.ExceptionHandling.throw400;
import static nl.utwente.exceptions.ExceptionHandling.throw404;

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
        try {
            return BookingDao.getSpecificBooking(bookingID);
        } catch (InvalidBookingIDException e) {
            throw404(e);
        }


        return null;
    }



    /**
     * Creates a booking.
     * @return Booking ID
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/create")
    public int createBooking(@Valid SpecifiedBooking booking) {
        try {
            return BookingDao.createBooking(booking);
        } catch (BookingException e) {
            throw400(e);
        }
        return 0;
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
        try {
            if (BookingDao.updateBooking(bookingID, booking)) {
                return booking;
            } else {
                throw new InternalServerErrorException("Something went wrong in updateBooking, but that's all we know");
            }
        } catch (InvalidBookingIDException e) {
            throw404(e);
        } catch (BookingException e) {
            throw400(e);
        }
        return null;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{bookingID}/participants")
    public List<User> getParticipants(@PathParam("bookingID") int bookingID) {
        try {
            return ParticipantDao.getParticipantsOfBooking(bookingID);
        } catch (InvalidBookingIDException e) {
            throw404(e);
        }

        return null;
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
        try {
            BookingDao.deleteBooking(bookingID);
        } catch (InvalidBookingIDException e) {
            throw404(e);
        }


    }
}
