package nl.utwente.resource;

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
import javax.ws.rs.core.*;
import java.util.List;
import java.util.Objects;

import static nl.utwente.dao.BookingDao.getEmailOfBookingOwnerFromBookingID;
import static nl.utwente.exceptions.ExceptionHandling.*;

@Path("/booking")
public class BookingResource {
    @Context HttpServletResponse response;
    @Context
    SecurityContext securityContext;


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
            return BookingDao.getOutputBooking(bookingID);
        } catch (InvalidBookingIDException e) {
            throw404(e.getMessage());
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
            throw400(e.getMessage());
        }
        return 0;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{bookingID}/participants")
    public List<User> getParticipants(@PathParam("bookingID") int bookingID) {
        try {
            return ParticipantDao.getParticipantsOfBooking(bookingID);
        } catch (InvalidBookingIDException e) {
            throw404(e.getMessage());
        }

        return null;
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
        if (securityContext.getUserPrincipal() != null) { // If logged in
            try {
                if (Objects.equals(securityContext.getUserPrincipal().getName(),
                getEmailOfBookingOwnerFromBookingID(bookingID))) { // If owner = logged in user

                    if (BookingDao.updateBooking(bookingID, booking)) {
                        return booking;
                    } else {
                        throw500("Something went wrong in updateBooking, but that's all we know");
                    }
                } else {
                    throw403("You are not authorized to edit this person's booking");
                }

            } catch (InvalidBookingIDException e) {
                throw404(e.getMessage());
            } catch (BookingException e) {
                throw400(e.getMessage());
            }
        } else {
            throw401("You are not logged in");
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
        if (securityContext.getUserPrincipal() != null) { // If logged in
            try {
                if (Objects.equals(securityContext.getUserPrincipal().getName(),
                    getEmailOfBookingOwnerFromBookingID(bookingID))) { // If owner = logged in user
                    BookingDao.deleteBooking(bookingID);
                } else {
                    throw403("You are not authorized to remove this person's booking");
                }
            } catch (InvalidBookingIDException e) {
                throw404(e.getMessage());
            }
        } else {
            throw401("You are not logged in");
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
    @Path("/admin/{bookingID}/update")
    public SpecifiedBooking adminUpdateBooking(
        @PathParam("bookingID") int bookingID,
        @Valid SpecifiedBooking booking
    ) {
        if (securityContext.getUserPrincipal() != null) { // If logged in
            try {
                if (securityContext.isUserInRole("ADMIN")) {

                    if (BookingDao.updateBooking(bookingID, booking)) {
                        return booking;
                    } else {
                        throw500("Something went wrong in updateBooking, but that's all we know");
                    }
                } else {
                    throw403("You are not authorized to edit this person's booking");
                }

            } catch (InvalidBookingIDException e) {
                throw404(e.getMessage());
            } catch (BookingException e) {
                throw400(e.getMessage());
            }
        } else {
            throw401("You are not logged in");
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
    @Path("/admin/{bookingID}")
    public void adminDeleteBooking(@PathParam("bookingID") int bookingID) {
        if (securityContext.getUserPrincipal() != null) { // If logged in
            try {
                if (securityContext.isUserInRole("ADMIN")) {
                    BookingDao.deleteBooking(bookingID);
                } else {
                    throw403("You are not an administrator");
                }
            } catch (InvalidBookingIDException e) {
                throw404(e.getMessage());
            }
        } else {
            throw401("You are not logged in");
        }


    }

}
