package nl.utwente.exceptions;

public class InvalidBookingIDException extends BookingException {

    int id;

    public InvalidBookingIDException(int id) {
        super("Invalid booking id: " + String.valueOf(id));
        this.id=id;

    }
}
