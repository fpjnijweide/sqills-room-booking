package nl.utwente.exceptions;

public class InvalidBookingIDException extends DAOException {

    private int id;

    public InvalidBookingIDException(int id) {
        super("Invalid booking id: " + String.valueOf(id));
        this.id=id;

    }

    /**
     * Gets id
     *
     * @return value of id
     */
    public int getId() {
        return id;
    }
}
