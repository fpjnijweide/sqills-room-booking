package nl.utwente.resource;

import nl.utwente.dao.UserDao;
import nl.utwente.model.UserAdministration;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.Map;

@Path("/user")
public class UserResource {
    @Context HttpServletResponse response;
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
    @Path("/test")
    public void test(@Context HttpHeaders hh){
        Map<String, Cookie> cookies = hh.getCookies();
        Cookie myCookie = cookies.get("NAME");
        System.out.println(myCookie);
    }
}
