package nl.utwente.model;

import java.sql.Date;
import java.sql.Time;

public class SimpleBooking extends TimeSlot{
    private int roomNumber;
    private Date date;
    private Time startTime;
    private Time endTime;

    public SimpleBooking(Time startTime, Time endTime, int roomNumber, Date date) {
        this.roomNumber = roomNumber;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public SimpleBooking(String startTime, String endTime, int roomNumber, String date) {
        this.roomNumber = roomNumber;
        this.startTime = Time.valueOf(startTime);
        this.endTime = Time.valueOf(endTime);
        this.date = Date.valueOf(date);
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


    @Override
    public String toString() {
        return "date: "+ this.date + " endTime:" + this.endTime + " startTime: " + this.startTime + " roomNumber: " + this.getRoomNumber();
    }
}
