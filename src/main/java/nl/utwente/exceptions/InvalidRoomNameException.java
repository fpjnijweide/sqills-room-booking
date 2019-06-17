package nl.utwente.exceptions;

public class InvalidRoomNameException extends RoomException {
    private String roomName;


    public InvalidRoomNameException(String roomName) {
        super("Invalid room name: " + roomName);
        this.roomName = roomName;
    }

    /**
     * Gets roomName
     *
     * @return value of roomName
     */
    public String getRoomName() {
        return roomName;
    }
}
