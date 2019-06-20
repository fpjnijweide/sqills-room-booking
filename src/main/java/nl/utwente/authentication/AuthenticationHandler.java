package nl.utwente.authentication;

import nl.utwente.dao.UserDao;
import nl.utwente.exceptions.InvalidBookingIDException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.ws.rs.core.SecurityContext;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Objects;

import static nl.utwente.dao.BookingDao.getEmailOfBookingOwner;

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

    public static byte[] hashPassword(String password, byte[] salt) {
        byte[] hash = null;
        try {
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            hash = factory.generateSecret(spec).getEncoded();
        } catch(NoSuchAlgorithmException e){
            System.err.println(e);
        } catch (InvalidKeySpecException e){
            System.err.println(e);
        }
        return hash;
    }

    public static byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }

    public static boolean checkCredentials(String email, String password){
        return checkByteArrays(UserDao.getHash(email), hashPassword(password, UserDao.getSalt(email)));
    }

    public static boolean checkByteArrays(byte[] firstArray, byte[] secondArray){
        return Arrays.equals(firstArray, secondArray);
    }
}
