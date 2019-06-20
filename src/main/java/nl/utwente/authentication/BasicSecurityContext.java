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
            public String getName() {
                return user.getName();
            }
        };
    }

    @Override
    public String getAuthenticationScheme() {
        return SecurityContext.BASIC_AUTH;
    }

    @Override
    public boolean isSecure() { return secure; }

    @Override
    public boolean isUserInRole(String role) {
        // TODO Improve
        if (Objects.equals(role, "ADMIN") || Objects.equals(role, "admin") ||
            Objects.equals(role, "administrator") || Objects.equals(role, "ADMINISTRATOR")){
            return user.isAdministrator();
        } else return Objects.equals(role, "USER") || Objects.equals(role, "user");
    }
}
