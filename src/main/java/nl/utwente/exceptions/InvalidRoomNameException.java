package nl.utwente.exceptions;

public class InvalidRoomNameException extends RoomException {
    String roomName;


    public InvalidRoomNameException(String roomName) {
        super("Invalid room name: " + roomName);
        this.roomName = roomName;
    }
}
