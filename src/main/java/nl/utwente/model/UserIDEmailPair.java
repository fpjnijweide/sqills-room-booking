package nl.utwente.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class UserIDEmailPair {
    @NotNull
    private int bookingid;
    @NotNull
    @Pattern(regexp = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$")
    private String email;

    public int getBookingid() {
        return bookingid;
    }

    public void setBookingid(int bookingid) {
        this.bookingid = bookingid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
