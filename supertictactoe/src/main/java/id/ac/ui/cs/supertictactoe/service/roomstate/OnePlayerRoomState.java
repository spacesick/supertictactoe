package id.ac.ui.cs.supertictactoe.service.roomstate;

import id.ac.ui.cs.supertictactoe.model.Room;
import id.ac.ui.cs.supertictactoe.model.RoomStatus;
import id.ac.ui.cs.supertictactoe.model.RoomTemp;
import id.ac.ui.cs.supertictactoe.model.User;

public class OnePlayerRoomState implements RoomState {

    public Room join(User newUser, RoomTemp roomTemp, Room room) {
        User playerOne = room.getPlayer1();
        if (!playerOne.getUserId().equals(newUser.getUserId())) {
            room.setPlayer2(newUser);
            roomTemp.setState(roomTemp.getFullRoomState());
            room.setStatus(RoomStatus.FULL);
        }
        return room;
    }

}