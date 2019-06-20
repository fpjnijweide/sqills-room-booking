package nl.utwente.authentication;

import nl.utwente.exceptions.InvalidBookingIDException;

import javax.ws.rs.core.SecurityContext;
import java.util.Objects;

import static nl.utwente.dao.BookingDao.getEmailOfBookingOwnerFromBookingID;

public class AuthenticationHandler {

    public static boolean userIsLoggedIn(SecurityContext securityContext) {
        return (securityContext.getUserPrincipal() != null);
    }

    public static boolean userOwnsBooking(SecurityContext securityContext, int bookingID) throws InvalidBookingIDException {

        return (Objects.equals(securityContext.getUserPrincipal().getName(),
            getEmailOfBookingOwnerFromBookingID(bookingID)));

    }

    public static boolean userIsAdmin(SecurityContext securityContext) {
        return securityContext.isUserInRole("ADMIN");
    }
}
