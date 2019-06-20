package nl.utwente.resource;

import nl.utwente.dao.UserDao;
import nl.utwente.model.EmailList;
import nl.utwente.model.UserAdministration;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.ArrayList;
import java.util.List;

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

    @POST
    @Path("/email/check")
    @Produces(MediaType.APPLICATION_JSON)
    public EmailList getInvalidEmails(EmailList emails){
        EmailList returnList = new EmailList();
        List<String> invalidEmails = new ArrayList<>();
        for(String email : emails.getEmails()){
            if (!UserDao.isValidEmail(email)){
                invalidEmails.add(email);
            }
        }
        returnList.setEmails(invalidEmails);
        return returnList;
    }


}
