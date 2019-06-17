package nl.utwente.resource;

import nl.utwente.dao.ParticipantDao;
import nl.utwente.exceptions.InvalidBookingIDException;
import nl.utwente.exceptions.InvalidEmailException;
import nl.utwente.exceptions.InvalidUserIDException;
import nl.utwente.model.UserIDBookingIDPair;
import nl.utwente.model.UserIDEmailPair;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/participant")
public class ParticipantResource {
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
            throw new NotFoundException(e.getMessage());
        } catch (InvalidEmailException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @DELETE
    @Path("/delete")
    @Consumes(MediaType.APPLICATION_JSON)
    public void removeParticipant(@Valid UserIDBookingIDPair pair) {
        try {
            ParticipantDao.removeParticipant(pair.getBookingid(), pair.getUserid());
        } catch (InvalidBookingIDException e) {
            throw new NotFoundException(e.getMessage());
        } catch (InvalidUserIDException e) {
            throw new BadRequestException(e.getMessage());
        }


    }
}
