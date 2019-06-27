package nl.utwente.authentication;

import nl.utwente.model.User;

import javax.ws.rs.core.SecurityContext;
import java.security.Principal;
import java.util.Objects;

public class BasicSecurityContext implements SecurityContext {
    private final User user;
    private final boolean secure;

    public BasicSecurityContext(User user, boolean secure) {
        this.user = user;
        this.secure = secure;
    }

    @Override
    public Principal getUserPrincipal() {
        return new Principal() {
            @Override
            // Yes, this returns the email instead of the name (the interface requires a getName)
            // However, the "username" that the user logs in with (his/her email)
            // should be the name that is relevant to the security. It is ugly.
            // Because of Jersey constraints, there is no other way to do this.

            public String getName() {
                return user.getEmail();
            }


        };
    }

    @Override
    public String getAuthenticationScheme() {
        return SecurityContext.BASIC_AUTH;
    }

    @Override
    public boolean isSecure() { return secure; }

    public User getUser(){
        return user;
    }

    @Override
    public boolean isUserInRole(String role) {
        // This method is completely awful. However, it needs to work this way
        // because otherwise, Jersey will complain. Don't question it.
        if (Objects.equals(role, "ADMIN") || Objects.equals(role, "admin") ||
            Objects.equals(role, "administrator") || Objects.equals(role, "ADMINISTRATOR")){
            return user.isAdministrator();
        } else return Objects.equals(role, "USER") || Objects.equals(role, "user");
    }
}
