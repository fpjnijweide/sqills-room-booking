package nl.utwente.exceptions;

public class InvalidUserIDException extends DAOException {

    private int id;

    public InvalidUserIDException(int id) {
        super("Invalid user id: " + String.valueOf(id));
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
