package nl.utwente.authentication;

import nl.utwente.exceptions.InvalidBookingIDException;
import nl.utwente.model.User;
import nl.utwente.model.UserIDBookingIDPair;

import javax.ws.rs.core.SecurityContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static nl.utwente.dao.BookingDao.getEmailOfBookingOwner;
import static nl.utwente.dao.ParticipantDao.getParticipantsOfBooking;

public class AuthenticationHandler {

    public static boolean userIsLoggedIn(SecurityContext securityContext) {
        return (securityContext.getUserPrincipal() != null);
    }

    public static boolean userOwnsBooking(SecurityContext securityContext, int bookingID) throws InvalidBookingIDException {

        return (Objects.equals(securityContext.getUserPrincipal().getName(),
            getEmailOfBookingOwner(bookingID)));

    }

    public static boolean userParticipatesInBooking(SecurityContext securityContext, int userID) {
        return (((BasicSecurityContext) securityContext).getUserID() == userID);
    }

//    public static boolean userParticipatesInBooking(SecurityContext securityContext, int bookingID) throws InvalidBookingIDException {
//        List<User> users = getParticipantsOfBooking(bookingID);
//        for (User u : users) {
//            if (u.getEmail().equals(securityContext.getUserPrincipal().getName())){
//                return true;
//            }
//        }
//        return false;
//    }

    public static boolean userIsAdmin(SecurityContext securityContext) {
        return securityContext.isUserInRole("ADMIN");
    }
}
