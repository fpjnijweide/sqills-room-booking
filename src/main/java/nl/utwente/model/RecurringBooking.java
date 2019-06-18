package nl.utwente.model;

import java.sql.Time;
import java.sql.Date;

public class RecurringBooking extends SpecifiedBooking {

    private RepeatUnits repeatEvery;
    private int frequency;
    private Date startingFrom;
    private Date endingAt;
    private int withGapsOf;

    public RecurringBooking(Time startTime, Time endTime, String roomName, java.sql.Date date, String email, boolean isPrivate, String title, int frequency, Date startingFrom, Date endingAt, int withGapsOf){
        super( startTime,  endTime,  roomName,  date,  email,  isPrivate,  title);
        this.frequency = frequency;
        this.startingFrom = startingFrom;
        this.endingAt = endingAt;
        this.withGapsOf = withGapsOf;
    }
    public RecurringBooking(){
    }
    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public Date getStartingFrom() {
        return startingFrom;
    }

    public void setStartingFrom(Date startingFrom) {
        this.startingFrom = startingFrom;
    }

    public Date getEndingAt() {
        return endingAt;
    }

    public void setEndingAt(Date endingAt) {
        this.endingAt = endingAt;
    }

    public int getWithGapsOf() {
        return withGapsOf;
    }

    public void setWithGapsOf(int withGapsOf) {
        this.withGapsOf = withGapsOf;
    }

    public RepeatUnits getRepeatEvery() {
        return repeatEvery;
    }

    public void setRepeatEvery(RepeatUnits repeatEvery) {
        this.repeatEvery = repeatEvery;
    }






}
 enum RepeatUnits {
    day,
    week,
    month,
    year
}