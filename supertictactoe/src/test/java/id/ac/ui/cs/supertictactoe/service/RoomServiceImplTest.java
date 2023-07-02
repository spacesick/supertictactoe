package id.ac.ui.cs.supertictactoe.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import id.ac.ui.cs.supertictactoe.model.Room;
import id.ac.ui.cs.supertictactoe.model.User;
import id.ac.ui.cs.supertictactoe.repository.RoomRepository;
import id.ac.ui.cs.supertictactoe.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoomServiceImplTest {

    @InjectMocks
    private RoomServiceImpl roomService;

    @Mock
    private RoomRepository roomRepository;
    
    @Mock
    private UserRepository userRepository;

    @Mock
    private User mockedUser1;

    @Mock
    private User mockedUser2;

    @Mock
    private Room mockedRoom;

    @Test
    void testCreateRoom() {
        mockedRoom =  roomService.createRoom();
        mockedRoom.setRoomName("Room0");
        when(roomRepository.save(any())).thenReturn(mockedRoom);

        Room expectedRoom = roomService.createRoom("Room0");
        assertEquals(mockedRoom.getRoomName(),expectedRoom.getRoomName());
    }
    
    @Test
    void testJoinRoom() {
        String mockUserId = "userid";
        String mockUserId2 = "userid2";
        String mockRoomId = "r0";
        mockedUser1 = new User();
        mockedUser2 = new User();
        mockedUser1.setUserId(mockUserId);
        mockedUser1.setPassword("password");
        mockedUser2.setUserId(mockUserId2);
        mockedUser2.setPassword("password");
        mockedRoom = new Room(mockRoomId, "Room0", null, null);
        when(userRepository.getReferenceById(mockUserId)).thenReturn(mockedUser1);
        when(userRepository.getReferenceById(mockUserId2)).thenReturn(mockedUser2);
        when(roomRepository.getReferenceById(mockRoomId)).thenReturn(mockedRoom);
        when(roomRepository.save(any())).thenReturn(mockedRoom);
        
        var room = roomService.joinRoom(mockUserId, mockRoomId);
        assertEquals(mockedUser1, room.getPlayer1());
        assertEquals(mockRoomId, room.getRoomId());
        var room2 = roomService.joinRoom(mockUserId2, mockRoomId);
        assertEquals(mockedUser2, room2.getPlayer2());
        var room3 = roomService.joinRoom(mockUserId2, mockRoomId);
        assertEquals(mockedUser2, room3.getPlayer2());
    }

    @Test
    void getRoomList() {
        List list = new ArrayList();
        when(roomRepository.findAll()).thenReturn(list);
        assertEquals(list, roomService.getRoomList());
    }

}