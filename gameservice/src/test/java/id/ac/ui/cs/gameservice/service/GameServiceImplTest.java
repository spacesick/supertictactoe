package id.ac.ui.cs.gameservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import id.ac.ui.cs.gameservice.model.Game;
import id.ac.ui.cs.gameservice.model.Mark;
import id.ac.ui.cs.gameservice.model.MoveStatus;
import id.ac.ui.cs.gameservice.model.Player;
import id.ac.ui.cs.gameservice.repository.GameRepository;
import id.ac.ui.cs.gameservice.service.GameServiceImpl;

@ExtendWith(MockitoExtension.class)
class GameServiceImplTest {

    @Mock
    private GameRepository gameRepository;

    @Mock
    private Game mockedGame;

    @InjectMocks
    private GameServiceImpl gameService;

    @Test
    void testGetGameById() {
        List<Mark> initMarkings = new ArrayList<>(Arrays.asList(new Mark[3 * 3]));
        Collections.fill(initMarkings, Mark.NONE);

        mockedGame = Game.builder()
                .winnerIndex(-1)
                .isOver(false)
                .currentPlayerTurnIndex(0)
                .size(3)
                .marks(initMarkings)
                .winLength(3)
                .player(Player.builder().userId("u0").mark(Mark.NOUGHT).build())
                .player(Player.builder().userId("u1").mark(Mark.CROSS).build())
                .build();

        mockedGame.setId("g0");

        when(gameRepository.findById("g0")).thenReturn(Optional.of(mockedGame));
        when(gameRepository.getReferenceById("g0")).thenReturn(mockedGame);

        Game calledGame = gameService.getGameById("g0");
        verify(gameRepository, times(1)).findById("g0");
        verify(gameRepository, times(1)).getReferenceById("g0");
        assertEquals(calledGame.getId(), mockedGame.getId());
    }

    @Test
    void testInitGame() {
        mockedGame = new Game();
        mockedGame.setId("g0");

        var playerUserIds = Arrays.asList("u0", "u1");
        var playerUsernames = Arrays.asList("user0", "user1");

        when(gameService.initGame(playerUserIds, playerUsernames, 3, 3)).thenReturn(mockedGame);
        Game builtGame = gameService.initGame(playerUserIds, playerUsernames, 3, 3);
        assertEquals(builtGame, mockedGame);
        assertEquals("g0", builtGame.getId());
        assertEquals(0, builtGame.getCurrentPlayerTurnIndex());
        assertEquals(false, builtGame.isOver());
        assertEquals(-1, builtGame.getWinnerIndex());
    }

    @Test
    void testDoMove() {
        List<Mark> initMarkings = new ArrayList<>(Arrays.asList(new Mark[3 * 3]));
        Collections.fill(initMarkings, Mark.NONE);

        mockedGame = Game.builder()
                .winnerIndex(-1)
                .isOver(false)
                .currentPlayerTurnIndex(0)
                .size(3)
                .marks(initMarkings)
                .winLength(3)
                .player(Player.builder().userId("u0").mark(Mark.NOUGHT).build())
                .player(Player.builder().userId("u1").mark(Mark.CROSS).build())
                .build();

        when(gameRepository.getReferenceById("g0")).thenReturn(mockedGame);

        MoveStatus moveStatus = new MoveStatus(false, Mark.NONE, null, false);
        lenient().when(gameService.doMove("g0", 3, "u0"))
                .thenReturn(moveStatus);

        MoveStatus returnedMoveStatus = gameService.doMove("g0", 3,
                "u0");
        assertEquals(returnedMoveStatus, moveStatus);
    }
}
