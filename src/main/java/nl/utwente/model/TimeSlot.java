package nl.utwente.model;

import javax.validation.constraints.NotNull;
import java.sql.Time;

public class TimeSlot {
    @NotNull
    protected Time startTime;
    @NotNull
    protected Time endTime;

    public TimeSlot(Time startTime, Time endTime){
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public TimeSlot() {

    }

    /**
     * Gets startTime
     *
     * @return value of startTime
     */
    public Time getStartTime() {
        return startTime;
    }

    /**
     * Sets startTime to startTime
     *
     * @param startTime new value of startTime
     */
    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    /**
     * Gets endTime
     *
     * @return value of endTime
     */
    public Time getEndTime() {
        return endTime;
    }

    /**
     * Sets endTime to endTime
     *
     * @param endTime new value of endTime
     */
    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "endTime:" + this.endTime + " startTime: " + this.startTime;
    }
}
