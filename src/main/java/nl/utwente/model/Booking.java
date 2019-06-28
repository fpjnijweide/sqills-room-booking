package nl.utwente.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.xml.bind.annotation.XmlRootElement;
import java.sql.Time;

@XmlRootElement
public class Booking extends TimeSlot {
    @NotNull
    @Pattern(regexp = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$")
    protected String email;
    protected boolean isPrivate;
    protected String title;


    public Booking(Time startTime, Time endTime, String email, boolean isPrivate, String title) {
        super(startTime, endTime);
        this.email = email;
        this.isPrivate = isPrivate;
        this.title = title;
    }

    public Booking() {

    }

    /**
     * Gets title
     *
     * @return value of title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets title to title
     *
     * @param title new value of title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets email
     *
     * @return value of email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets email to email
     *
     * @param email new value of email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets getIsPrivate
     *
     * @return value of getIsPrivate
     */
    public boolean getIsPrivate() {
        return isPrivate;
    }

    /**
     * Sets getIsPrivate to aPrivate
     *
     * @param aPrivate new value of getIsPrivate
     */
    public void setIsPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    @Override
    public String toString() {
        return "endTime:" + this.endTime + " startTime: " + this.startTime + " email: " + this.email + " getIsPrivate: " + this.isPrivate + " title: " + this.title;
    }
}
