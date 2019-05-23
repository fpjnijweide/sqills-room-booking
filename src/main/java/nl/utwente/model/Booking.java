package nl.utwente.model;

import java.sql.Date;
import java.sql.Time;

public class Booking {
    protected Time startTime;
    protected Time endTime;
    protected String email;
    protected boolean isPrivate;

    public Booking(Time startTime, Time endTime, String email, boolean isPrivate) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.email = email;
        this.isPrivate = isPrivate;
    }

    public Booking() {

    }



    public Time getStartTime() {
        return startTime;
    }

    public Time getEndTime() {
        return endTime;
    }


    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(Time endTime) {
        this.endTime = endTime;
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
     * Gets isPrivate
     *
     * @return value of isPrivate
     */
    public boolean isPrivate() {
        return isPrivate;
    }

    /**
     * Sets isPrivate to aPrivate
     *
     * @param aPrivate new value of isPrivate
     */
    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    @Override
    public String toString() {
        return "endTime:" + this.endTime + " startTime: " + this.startTime + " email: " + this.email + " isPrivate: " + this.isPrivate;
    }
}
