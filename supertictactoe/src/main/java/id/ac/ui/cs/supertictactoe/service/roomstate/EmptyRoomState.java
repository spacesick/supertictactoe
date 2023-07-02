package id.ac.ui.cs.supertictactoe.service.roomstate;

import id.ac.ui.cs.supertictactoe.model.Room;
import id.ac.ui.cs.supertictactoe.model.RoomStatus;
import id.ac.ui.cs.supertictactoe.model.RoomTemp;
import id.ac.ui.cs.supertictactoe.model.User;

public class EmptyRoomState implements RoomState {

    public Room join(User newUser, RoomTemp roomTemp, Room room) {
        room.setPlayer1(newUser);
        roomTemp.setState(roomTemp.getOnePlayerRoomState());
        room.setStatus(RoomStatus.ONE_PLAYER);
        return room;

    }
}
