package id.ac.ui.cs.supertictactoe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import id.ac.ui.cs.supertictactoe.model.Room;
import id.ac.ui.cs.supertictactoe.model.RoomTemp;
import id.ac.ui.cs.supertictactoe.repository.RoomRepository;
import id.ac.ui.cs.supertictactoe.repository.UserRepository;

import java.util.List;

@Service
public class RoomServiceImpl implements RoomService {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private UserRepository userRepository;

    public final Room createRoom() {
        return new Room();
    }

    public final Room createRoom(final String roomName) {
        var room = new Room();
        room.setRoomName(roomName);
        return roomRepository.save(room);
    }

    public final Room joinRoom(final String userId, final String roomId) {

        var room = roomRepository.getReferenceById(roomId);
        var newPlayer = userRepository.getReferenceById(userId);
        var roomTemp = RoomTemp.fromJPA(room);

        roomTemp.join(newPlayer, room);

        return roomRepository.save(room);
    }

    public final List<Room> getRoomList() {
        return roomRepository.findAll();
    }

}
