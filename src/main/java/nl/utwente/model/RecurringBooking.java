package nl.utwente.model;

import java.sql.Time;
import java.sql.Date;

public class RecurringBooking extends SpecifiedBooking {

    private String repeatEveryType;
    private int repeatEvery;
    private Date endingAt;

    public RecurringBooking(Time startTime, Time endTime, String roomName, java.sql.Date date, String email, boolean isPrivate, String title, String repeatEveryType, int repeatEvery, Date endingAt){
        super( startTime,  endTime,  roomName,  date,  email,  isPrivate,  title);
        this.repeatEvery = repeatEvery;
        this.repeatEveryType = repeatEveryType;
        this.endingAt = endingAt;
    }

    public RecurringBooking(){
    }

    public int getRepeatEvery() {
        return repeatEvery;
    }

    public void setRepeatEvery(int repeatEvery) {
        this.repeatEvery = repeatEvery;
    }

    public Date getEndingAt() {
        return endingAt;
    }

    public void setEndingAt(Date endingAt) {
        this.endingAt = endingAt;
    }


    public String getRepeatEveryType() {
        return repeatEveryType;
    }

    public void setRepeatEveryType(String repeatEveryType) {
        this.repeatEveryType = repeatEveryType;
    }


    @Override
    public String toString() {
        return "Repeat type: "+ this.repeatEveryType + "Repeat type: " + this.repeatEvery;
    }
}
 enum RepeatUnits {
    day,//0
    week,//1
    month,//2
    year//3
}