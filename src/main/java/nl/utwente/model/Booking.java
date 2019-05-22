package nl.utwente.model;

import java.sql.Date;
import java.sql.Time;

public class Booking {
    private int roomNumber;
    private Date date;
    private Time startTime;
    private Time endTime;
    private String email;
    private boolean isPrivate;

    public Booking(Time startTime, Time endTime, int roomNumber, Date date, String email, boolean isPrivate) {
        this.roomNumber = roomNumber;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.email = email;
        this.isPrivate = isPrivate;
    }

    public Booking() {

    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public Date getDate() {
        return date;
    }

    public Time getStartTime() {
        return startTime;
    }

    public Time getEndTime() {
        return endTime;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public void setDate(Date date) {
        this.date = date;
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
        return "date: "+ this.date + "endTime:" + this.endTime + " startTime" + this.startTime + " roomNumber: " + this.getRoomNumber() + " email: " + this.email + "private: " + this.isPrivate;
    }
}
