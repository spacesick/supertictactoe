package id.ac.ui.cs.supertictactoe.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import id.ac.ui.cs.supertictactoe.model.Game;
import id.ac.ui.cs.supertictactoe.model.Room;
import id.ac.ui.cs.supertictactoe.model.User;
import id.ac.ui.cs.supertictactoe.model.dto.GameDTO;
import id.ac.ui.cs.supertictactoe.repository.RoomRepository;

@ExtendWith(MockitoExtension.class)
class GameServiceImplTest {

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private User mockedUser1;

    @Mock
    private User mockedUser2;

    @Mock
    private Room mockedRoom;

    @Mock
    private Game mockedGame;

    @Mock
    private GameDTO mockedGameDto;

    @InjectMocks
    @Spy
    private GameServiceImpl gameService;

    @Test
    void testGetRoomById() {
        mockedRoom = new Room("r0", "Room0", mockedUser1, mockedUser2);
        when(roomRepository.findById("r0")).thenReturn(Optional.of(mockedRoom));
        when(roomRepository.getReferenceById("r0")).thenReturn(mockedRoom);

        when(gameService.getRoomById("r0")).thenReturn(mockedRoom);
        Room calledRoom = gameService.getRoomById("r0");
        verify(roomRepository, times(1)).findById("r0");
        verify(roomRepository, times(1)).getReferenceById("r0");
        assertEquals(calledRoom.getRoomId(), mockedRoom.getRoomId());
    }
}
