package id.ac.ui.cs.supertictactoe.service;


import java.util.List;

import id.ac.ui.cs.supertictactoe.model.Room;

public interface RoomService {

    Room createRoom();

    Room createRoom(String roomName);

    Room joinRoom(String userId, String roomId);

    List<Room> getRoomList();

}
