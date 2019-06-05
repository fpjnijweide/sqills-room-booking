package nl.utwente.resource;

import nl.utwente.dao.UserDao;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/user")
public class UserResource {
    public UserResource(){

    }

    @GET
    @Path("/{email}")
    @Produces(MediaType.APPLICATION_JSON)
    /**
     * Returns all of today's bookings for a specific room.
     * @param roomNumber Number specifying the room
     * @return JSON object containing all of today's bookings for a specific room
     */
    public String getUserList (@PathParam("email") String email) {
        return "{ \"email\":" + "\"" + UserDao.getEmail(email) + "\""+ "}";
    }
}
