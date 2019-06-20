package nl.utwente.resource;

import nl.utwente.dao.ParticipantDao;
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

import static nl.utwente.exceptions.ExceptionHandling.throw400;
import static nl.utwente.exceptions.ExceptionHandling.throw404;

@Path("/participant")
public class ParticipantResource {
    @Context
    SecurityContext securityContext;

    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    public UserIDEmailPair addParticipant(@Valid UserIDEmailPair pair) {
        try {
            if (ParticipantDao.addParticipantEmailToBooking(pair.getBookingid(), pair.getEmail())) {
                return pair;
            } else {
                throw new InternalServerErrorException("Something went wrong in addParticipant");
            }
        } catch (InvalidBookingIDException e) {
            throw404(e);
        } catch (InvalidEmailException e) {
            throw400(e);
        }
        return null;
    }

    @DELETE
    @Path("/delete")
    @Consumes(MediaType.APPLICATION_JSON)
    public void removeParticipant(@Valid UserIDBookingIDPair pair) {
        try {
            ParticipantDao.removeParticipant(pair.getBookingid(), pair.getUserid());
        } catch (InvalidBookingIDException e) {
            throw404(e);
        } catch (InvalidUserIDException e) {
            throw400(e);
        }


    }
}
