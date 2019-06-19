package nl.utwente.resource;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import nl.utwente.dao.UserDao;
import nl.utwente.model.UserAdministration;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

@Path("/user")
public class UserResource {
    @Context HttpServletResponse response;
    public UserResource(){

    }

    // TODO refactor this method's name and javascript
    @GET
    @Path("/{email}")
    @Produces(MediaType.APPLICATION_JSON)
    /**
     * Returns all of today's bookings for a specific room.
     * @param roomNumber Number specifying the room
     * @return JSON object containing all of today's bookings for a specific room
     */
    public String getUserList (@PathParam("email") String incompleteEmail) {
        return "{ \"email\":" + "\"" + UserDao.getEmail(incompleteEmail) + "\""+ "}";
    }

    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(UserAdministration loginAttempt){
        int myInt = UserDao.checkCredentials(loginAttempt.getUsername(), loginAttempt.getPassword()) ? 1 : 0;
        Cookie cookie = new Cookie("NAME", "123");
        NewCookie cook = new NewCookie(cookie, "123", 5*60, true);
        return Response.ok("OK").cookie(cook).build();
    }

    @GET
    @Path("/validateEmail/{email}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getIsValidEmail(@PathParam("email") String email) {
        boolean result = UserDao.isValidEmail(email);

        final JsonNodeFactory factory = JsonNodeFactory.instance;
        ObjectNode isValid = factory.objectNode();
        isValid.put("valid", result);
        return isValid.toString();

    }
}
