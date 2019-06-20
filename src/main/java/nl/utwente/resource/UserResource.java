package nl.utwente.resource;

import nl.utwente.authentication.AuthenticationFilter;
import nl.utwente.dao.UserDao;
import nl.utwente.model.UserAdministration;

import javax.annotation.Priority;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

import static nl.utwente.exceptions.ExceptionHandling.throwForbidden;

@Path("/user")
@Priority(Priorities.AUTHENTICATION)
public class UserResource {
    @Context
    HttpServletRequest request;
    @Context HttpServletResponse response;
    @Context
    SecurityContext securityContext;

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
    public void login(UserAdministration loginAttempt){
        boolean result = UserDao.checkCredentials(loginAttempt.getUsername(), loginAttempt.getPassword());
        if (result) {
            HttpSession session = request.getSession(true);
            session.setAttribute(AuthenticationFilter.principalName, loginAttempt.getUsername());
        } else {
            throwForbidden();
        }
//        Cookie cookie = new Cookie("NAME", "123");
//        NewCookie cook = new NewCookie(cookie, "123", 5*60, true);
//        return Response.ok("OK").cookie(cook).build();

    }

    @GET
    @Path("/logout")
    public void logout() {
        HttpSession session = request.getSession(true);
        session.setAttribute(AuthenticationFilter.principalName, null);
    }

}
