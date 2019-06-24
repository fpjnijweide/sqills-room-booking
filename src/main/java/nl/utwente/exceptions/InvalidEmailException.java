package nl.utwente.exceptions;

public class InvalidEmailException extends Exception {

    private String email;

    public InvalidEmailException(String email) {
        super("Invalid email: " + email);
        this.email=email;

    }

    /**
     * Gets email
     *
     * @return value of email
     */
    public String getEmail() {
        return email;
    }
}
