package nl.utwente.resource;

import nl.utwente.authentication.BasicSecurityContext;
import nl.utwente.dao.ParticipantDao;
import nl.utwente.exceptions.DAOException;
import nl.utwente.exceptions.InvalidBookingIDException;
import nl.utwente.exceptions.InvalidEmailException;
import nl.utwente.exceptions.InvalidUserIDException;
import nl.utwente.model.UserIDBookingIDPair;
import nl.utwente.model.UserIDEmailPair;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import static nl.utwente.authentication.AuthenticationHandler.*;
import static nl.utwente.exceptions.ExceptionHandling.*;

@Path("/participant")
public class ParticipantResource {
    @Context
    SecurityContext securityContext;

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
    public void removeParticipant(@Valid UserIDBookingIDPair pair) {
        // admin
        // participants or owner
        try {
            if (!userIsLoggedIn(securityContext)) {
                throw401("You are not logged in");
            }
            if ((!userParticipatesInBooking(securityContext, pair.getUserid()) &&
                (!userOwnsBooking(securityContext,pair.getBookingid()))) &&
                (!userIsAdmin(securityContext)))
            { // If owner = logged in user
                throw403("You are not authorized to edit this booking");
            }
            ParticipantDao.removeParticipant(pair.getBookingid(), pair.getUserid());
        } catch (InvalidBookingIDException e) {
            throw404(e.getMessage());
        } catch (InvalidUserIDException e) {
            throw400(e.getMessage());
        } catch (DAOException e) {
            throw500("Something went terribly wrong");
        }


    }
}
