package nl.utwente.resource;

import nl.utwente.dao.ParticipantDao;
import nl.utwente.exceptions.DAOException;
import nl.utwente.exceptions.InvalidBookingIDException;
import nl.utwente.exceptions.InvalidEmailException;
import nl.utwente.exceptions.InvalidUserIDException;
import nl.utwente.model.UserIDBookingIDPair;
import nl.utwente.model.UserIDEmailPair;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

import static nl.utwente.authentication.AuthenticationHandler.*;
import static nl.utwente.exceptions.ExceptionHandling.*;

@Path("/participant")
public class ParticipantResource {
    @Context
    SecurityContext securityContext;

    @Context
    ContainerRequestContext context;


    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    public UserIDEmailPair addParticipant(@Valid UserIDEmailPair pair) {
        try {
            ParticipantDao.addParticipantEmailToBooking(pair.getBookingid(), pair.getEmail());
            return pair;
        } catch (InvalidBookingIDException e) {
            throw404(e.getMessage());
        } catch (InvalidEmailException e) {
            throw400(e.getMessage());
        } catch (DAOException e) {
            throw500("Something went terribly wrong");
        }
        return null;
    }

    @DELETE
    @Path("/delete")
    @Consumes(MediaType.APPLICATION_JSON)
    public void removeParticipant(@QueryParam("userid") int userID, @QueryParam("bookingid") int bookingID) {
        // admin
        // participants or owner
        try {
            if (!userIsLoggedIn(securityContext)) {
                throw401("You are not logged in");
            }
//            if ((!userParticipatesInBooking(context, pair) &&
//                (!userOwnsBooking(securityContext,pair.getBookingid()))) &&
////            if ((!userParticipatesInBooking(securityContext, userID) &&
            if (!   ParticipantDao.userParticipatesInBooking(bookingID, userID) &&
            (!userOwnsBooking(securityContext,bookingID)) &&
                (!userIsAdmin(securityContext)))
            { // If owner = logged in user
                throw403("You are not authorized to edit this booking");
            }
            ParticipantDao.removeParticipant(bookingID, userID  );
        } catch (InvalidBookingIDException e) {
            throw404(e.getMessage());
        } catch (InvalidUserIDException e) {
            throw400(e.getMessage());
        } catch (DAOException e) {
            throw500("Something went terribly wrong");
        }


    }
}
