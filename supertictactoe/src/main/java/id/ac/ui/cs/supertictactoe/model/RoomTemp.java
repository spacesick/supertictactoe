package id.ac.ui.cs.supertictactoe.model;

import id.ac.ui.cs.supertictactoe.service.roomstate.EmptyRoomState;
import id.ac.ui.cs.supertictactoe.service.roomstate.FullRoomState;
import id.ac.ui.cs.supertictactoe.service.roomstate.OnePlayerRoomState;
import id.ac.ui.cs.supertictactoe.service.roomstate.RoomState;

public class RoomTemp {

    private EmptyRoomState emptyRoomState;
    private OnePlayerRoomState onePlayerRoomState;
    private FullRoomState fullRoomState;
    private RoomState currentState;

    public static RoomTemp fromJPA(Room room) {
        var roomTemp = new RoomTemp();
        switch (room.getStatus()) {
            case EMPTY:
                roomTemp.setState(roomTemp.getEmptyRoomState());
                break;
            case ONE_PLAYER:
                roomTemp.setState(roomTemp.getOnePlayerRoomState());
                break;
            case FULL:
                roomTemp.setState(roomTemp.getFullRoomState());
                break;
            default:
                break;
        }
        return roomTemp;
    }

    public RoomTemp() {
        emptyRoomState = new EmptyRoomState();
        onePlayerRoomState = new OnePlayerRoomState();
        fullRoomState = new FullRoomState();
        currentState = emptyRoomState;
    }

    public Room join(User newUser, Room room) {
        return this.currentState.join(newUser, this, room);
    }

    public void setState(RoomState newState) {
        this.currentState = newState;
    }

    public EmptyRoomState getEmptyRoomState() {
        return emptyRoomState;
    }

    public OnePlayerRoomState getOnePlayerRoomState() {
        return onePlayerRoomState;
    }

    public FullRoomState getFullRoomState() {
        return fullRoomState;
    }
}
