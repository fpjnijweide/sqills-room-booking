package nl.utwente.model;

import java.sql.Date;
import java.sql.Time;

public class OutputBooking extends TimeSlot {
    protected String userName;
    protected String roomName;
    protected Date date;
    protected String title;

    public int getBookingid() {
        return bookingid;
    }

    public void setBookingid(int bookingid) {
        this.bookingid = bookingid;
    }

    protected int bookingid;

    public OutputBooking(Time startTime, Time endTime, String userName, String roomName, Date date, String title, int bookingid) {
        super(startTime, endTime);
        this.userName = userName;
        this.roomName = roomName;
        this.date = date;
        this.title = title;
        this.bookingid = bookingid;
    }

    public OutputBooking() {

    }

    /**
     * Gets userName
     *
     * @return value of userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets userName to userName
     *
     * @param userName new value of userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Gets roomName
     *
     * @return value of roomName
     */
    public String getRoomName() {
        return roomName;
    }

    /**
     * Sets roomName to roomName
     *
     * @param roomName new value of roomName
     */
    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    /**
     * Gets date
     *
     * @return value of date
     */
    public Date getDate() {
        return date;
    }

    /**
     * Sets date to date
     *
     * @param date new value of date
     */
    public void setDate(Date date) {
        this.date = date;
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

    @Override
    public String toString() {
        return "endTime:" + this.endTime + " startTime: " + this.startTime + " userName: " + this.userName + " roomName: " + this.roomName + " date: " + this.date + " title: " + this.title;
    }
}
