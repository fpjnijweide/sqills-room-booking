package nl.utwente.resource;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import nl.utwente.authentication.AuthenticationFilter;
import nl.utwente.authentication.AuthenticationHandler;
import nl.utwente.dao.UserDao;
import nl.utwente.exceptions.DAOException;
import nl.utwente.google.GoogleAuth;
import nl.utwente.model.EmailList;
import nl.utwente.model.User;
import nl.utwente.model.UserAdministration;

import javax.annotation.Priority;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import static nl.utwente.dao.UserDao.insertUser;
import static nl.utwente.exceptions.ExceptionHandling.*;

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

    // This code was used back when we had passwords
//    @POST
//    @Path("/")
//    @Produces(MediaType.APPLICATION_JSON)
//    public void createUser(InputUser user) {
//        // This method is no longer used as we use Google Authentication
//        // TODO do input validation here
//        if (!user.isAdmin() || securityContext.isUserInRole("ADMIN")) {
//            try {
//                insertUser(user.getFullName(), user.getUsername(), user.getPassword(), user.isAdmin());
//            } catch (DAOException e) {
//                throw500("Something went terribly wrong");
//            }
//        } else {
//            throw403("You are not allowed to make admin users");
//        }
//
//    }

    // TODO refactor this method's name and javascript
    @GET
    @Path("/{email}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getUserList (@PathParam("email") String incompleteEmail) {
        try {
            return "{ \"email\":" + "\"" + UserDao.getEmail(incompleteEmail) + "\""+ "}";
        } catch (DAOException e) {
            throw500("Something went terribly wrong");
        }
        return null;
    }

    // This was used back when we had passwords
//    @POST
//    @Path("/login")
//    @Produces(MediaType.APPLICATION_JSON)
//    public void login(UserAdministration loginAttempt){
//        boolean result = false;
//        try {
//            result = AuthenticationHandler.checkCredentials(loginAttempt.getUsername(), loginAttempt.getPassword());
//        } catch (DAOException e) {
//            throw500("Something went terribly wrong");
//        }
//        if (result) {
//            HttpSession session = request.getSession(true);
//            session.setAttribute(AuthenticationFilter.principalName, loginAttempt.getUsername());
//        } else {
//            throw401("Incorrect login");
//        }
//    }

    @POST
    @Path("/logout")
    public void logout() {
        HttpSession session = request.getSession();
        session.removeAttribute(AuthenticationFilter.principalName);
        session.invalidate();
        securityContext = new SecurityContext() {
            public boolean isUserInRole(String role) {
                return false;
            }

            public boolean isSecure() {
                return false;
            }

            public Principal getUserPrincipal() {
                return null;
            }

            public String getAuthenticationScheme() {
                return null;
            }
        };
    }

    @POST
    @Path("/email/check")
    @Produces(MediaType.APPLICATION_JSON)
    public EmailList getInvalidEmails(EmailList emails){
        EmailList returnList = new EmailList();
        List<String> invalidEmails = new ArrayList<>();
        for(String email : emails.getEmails()){
            try {
                if (!UserDao.isValidEmail(email)){
                    invalidEmails.add(email);
                }
            } catch (DAOException e) {
                throw500("Something went terribly wrong");
            }
        }
        returnList.setEmails(invalidEmails);
        return returnList;
    }

    @GET
    @Path("/validateEmail/{email}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getIsValidEmail(@PathParam("email") String email) {
        boolean result = false;
        try {
            result = UserDao.isValidEmail(email);
        } catch (DAOException e) {
            throw500("Something went terribly wrong");
        }

        final JsonNodeFactory factory = JsonNodeFactory.instance;
        ObjectNode isValid = factory.objectNode();
        isValid.put("valid", result);
        return isValid.toString();

    }

    @POST
    @Path("/googleauth")
    @Produces(MediaType.APPLICATION_JSON)
    public String getGoogleAuthToken(String gAuthToken){
        GoogleIdToken googleIdToken = null;
        try {
            googleIdToken = GoogleAuth.getToken(gAuthToken);
        } catch (Exception e) {
            e.printStackTrace();
            throw401("Incorrect login");
        }
        String email = GoogleAuth.getUser(googleIdToken);
        HttpSession session = request.getSession(true);
        session.setAttribute(AuthenticationFilter.principalName, email);
        return "Token Received";
    }
    // TODO ERROR HANDLING
    @GET
    @Path("/u/{userID}")
    @Produces(MediaType.APPLICATION_JSON)
    public User getUserFromID(@PathParam("userID") int userID) {
        return UserDao.getUser(userID);
    }

    @DELETE
    @Path("/{userID}")
    public void deleteUser(@PathParam("userID") int userID) {
        UserDao.deleteUser(userID);
    }
}
