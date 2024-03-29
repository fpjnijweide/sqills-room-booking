package nl.utwente.model;

import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.sql.Time;

public class SpecifiedBooking extends Booking {
    @NotNull
    private String roomName;
    @NotNull
    private Date date;

    public SpecifiedBooking(Time startTime, Time endTime, String roomName, Date date, String email, boolean isPrivate, String title) {
        super(startTime, endTime, email, isPrivate, title);
        this.roomName = roomName;
        this.date = date;
    }

    public SpecifiedBooking() {

    }

    /**
     * Gets roomID
     *
     * @return value of roomID
     */
    public String getRoomName() {
        return roomName;
    }

    /**
     * Sets roomID to roomID
     *
     * @param roomName new value of roomID
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

    @Override
    public String toString() {
        return "endTime:" + this.endTime + " startTime: " + this.startTime + " email: " + this.email + " getIsPrivate: " + this.isPrivate + " roomName: " + this.roomName + " date: " + this.date + "title: " + this.title;
    }
}
