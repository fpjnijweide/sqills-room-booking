package nl.utwente.model;

import java.sql.Time;
import java.sql.Date;

public class SpecifiedBooking extends Booking {

    private String roomID;
    private Date date;

    public SpecifiedBooking(Time startTime, Time endTime, String roomID, Date date, String email, boolean isPrivate) {
        super(startTime, endTime, email, isPrivate);
        this.roomID = roomID;
        this.date = date;
    }

    public SpecifiedBooking() {

    }

    /**
     * Gets roomID
     *
     * @return value of roomID
     */
    public String getRoomID() {
        return roomID;
    }

    /**
     * Sets roomID to roomID
     *
     * @param roomID new value of roomID
     */
    public void setRoomID(String roomID) {
        this.roomID = roomID;
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


}
