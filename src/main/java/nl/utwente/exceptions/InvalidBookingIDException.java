package nl.utwente.exceptions;

public class InvalidBookingIDException extends DAOException {

    int id;

    public InvalidBookingIDException(int id) {
        super("Invalid booking id: " + String.valueOf(id));
        this.id=id;

    }
}
