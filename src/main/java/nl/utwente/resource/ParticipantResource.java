package nl.utwente.resource;

import nl.utwente.dao.ParticipantDao;
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
        if (ParticipantDao.addParticipantEmailToBooking(pair.getBookingid(), pair.getEmail())){
            return pair;
        } else {
            throw new BadRequestException();
        }
    }

    @DELETE
    @Path("/delete")
    @Consumes(MediaType.APPLICATION_JSON)
    public void removeParticipant(@Valid UserIDBookingIDPair pair) {
        if (!ParticipantDao.removeParticipant(pair.getBookingid(), pair.getUserid())){
            throw new BadRequestException();
        }
    }
}
