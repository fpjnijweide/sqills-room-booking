package nl.utwente.resource;

import nl.utwente.dao.ParticipantDao;
import nl.utwente.model.UserIDBookingIDPair;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

@Path("/participant")
public class ParticipantResource {
    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    public boolean addParticipant(UserIDBookingIDPair pair) {
        return ParticipantDao.addParticipantToBooking(pair.getBookingid(), pair.getUserid());
    }
}
