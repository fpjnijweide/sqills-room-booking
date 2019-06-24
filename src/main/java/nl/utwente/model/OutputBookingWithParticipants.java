package nl.utwente.model;

import java.util.List;

public class OutputBookingWithParticipants extends OutputBooking {
    private List<User> participants;

    public List<User> getParticipants() {
        return participants;
    }

    public void setParticipants(List<User> participants) {
        this.participants = participants;
    }
}
