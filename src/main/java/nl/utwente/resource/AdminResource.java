package nl.utwente.resource;

import nl.utwente.dao.BookingDao;
import nl.utwente.dao.ParticipantDao;
import nl.utwente.exceptions.BookingException;
import nl.utwente.exceptions.DAOException;
import nl.utwente.exceptions.InvalidBookingIDException;
import nl.utwente.exceptions.InvalidUserIDException;
import nl.utwente.model.SpecifiedBooking;
import nl.utwente.model.UserIDBookingIDPair;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

import static nl.utwente.authentication.AuthenticationHandler.*;
import static nl.utwente.exceptions.ExceptionHandling.*;

@Path("/admin")
public class AdminResource {
    @Context
    HttpServletResponse response;
    @Context
    SecurityContext securityContext;


    /**
     * Updates a specified booking.
     * @param bookingID ID specifying the booking to be updated
     * @return JSON object containing a "success" boolean field specifying whether the booking was
     *         successfully updated
     */
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/booking/{bookingID}/update")
    public SpecifiedBooking adminUpdateBooking(
        @PathParam("bookingID") int bookingID,
        @Valid SpecifiedBooking booking
    ) {
        try {
            if (!userIsLoggedIn(securityContext)) {
                throw401("You are not logged in");
            }
            if (!userIsAdmin(securityContext)) {
                throw403("You are not an administrator");
            }
            BookingDao.updateBooking(bookingID, booking);
        } catch (InvalidBookingIDException e) {
            throw404(e.getMessage());
        } catch (BookingException e) {
            throw400(e.getMessage());
        } catch (DAOException e) {
            throw500(e.getMessage());
        }

        return booking;
    }

    /**
     * Deletes a specific booking
     * @param bookingID Path parameter specifying the booking to be deleted
     * @return JSON object containing a "success" boolean field specifying whether the booking was
     *         successfully deleted
     */
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/booking/{bookingID}")
    public void adminDeleteBooking(@PathParam("bookingID") int bookingID) {
        try {
            if (!userIsLoggedIn(securityContext)) {
                throw401("You are not logged in");
            }
            if (!userIsAdmin(securityContext)) { // If owner = logged in user
                throw403("You are not an administrator");
            }
            BookingDao.deleteBooking(bookingID);
        } catch (InvalidBookingIDException e) {
            throw404(e.getMessage());
        } catch (DAOException e) {
            throw500(e.getMessage());
        }
    }

    @DELETE
    @Path("/participant/delete")
    @Consumes(MediaType.APPLICATION_JSON)
    public void adminRemoveParticipant(@Valid UserIDBookingIDPair pair) {
        // admin
        // participants or owner
        try {
            if (!userIsLoggedIn(securityContext)) {
                throw401("You are not logged in");
            }
            if (!userIsAdmin(securityContext)) {
                throw403("You are not an administrator");
            }
            ParticipantDao.removeParticipant(pair.getBookingid(), pair.getUserid());
        } catch (InvalidBookingIDException e) {
            throw404(e.getMessage());
        } catch (InvalidUserIDException e) {
            throw400(e.getMessage());
        } catch (DAOException e) {
            throw500(e.getMessage());
        }


    }
}
