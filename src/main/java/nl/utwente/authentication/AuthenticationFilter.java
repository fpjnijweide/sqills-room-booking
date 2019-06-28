package nl.utwente.authentication;


import nl.utwente.dao.UserDao;
import nl.utwente.exceptions.DAOException;
import nl.utwente.exceptions.InvalidEmailException;
import nl.utwente.model.User;

import javax.annotation.Priority;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
@Priority(Priorities.AUTHENTICATION)  // needs to happen before authorization
public class AuthenticationFilter implements ContainerRequestFilter {
    public static String principalName = "principalName";
    @Context
    HttpServletRequest httpRequest;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        HttpSession session = httpRequest.getSession(true);

        final String email = (String) session.getAttribute(principalName);
        if (email != null) {
            User user = null;
            try {
                user = UserDao.getUserFromEmail(email);
                requestContext.setProperty("user", user);
                requestContext.setSecurityContext(new BasicSecurityContext(user, false));
            } catch (InvalidEmailException e) {
                requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
            } catch (DAOException e) {
                requestContext.abortWith(Response.status(Response.Status.INTERNAL_SERVER_ERROR).build());

            }

        }


    }
}
