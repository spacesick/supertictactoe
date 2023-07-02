package id.ac.ui.cs.supertictactoe.service.roomstate;

import id.ac.ui.cs.supertictactoe.model.Room;
import id.ac.ui.cs.supertictactoe.model.RoomTemp;
import id.ac.ui.cs.supertictactoe.model.User;

public class FullRoomState implements RoomState {

    public Room join(User newUser, RoomTemp roomTemp, Room room) {
        return room;
    }

}