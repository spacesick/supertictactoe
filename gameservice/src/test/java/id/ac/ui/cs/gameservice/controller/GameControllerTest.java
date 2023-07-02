package id.ac.ui.cs.gameservice.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.test.web.servlet.MockMvc;

import id.ac.ui.cs.gameservice.controller.GameController;
import id.ac.ui.cs.gameservice.model.Game;
import id.ac.ui.cs.gameservice.model.Mark;
import id.ac.ui.cs.gameservice.model.MoveStatus;
import id.ac.ui.cs.gameservice.model.dto.GameCreationDTO;
import id.ac.ui.cs.gameservice.service.GameService;

@GraphQlTest(controllers = GameController.class)
class GameControllerTest {

    @Autowired
    private GraphQlTester graphQlTester;

    @Mock
    private Game mockedGame;

    @Mock
    private MoveStatus mockedMoveStatus;

    @MockBean
    private GameService gameService;

    @Test
    void testPlayNewGame() throws Exception {
        mockedGame = new Game();
        mockedGame.setId("g0");

        var playerUserIds = Arrays.asList("u0", "u1");
        var playerUsernames = Arrays.asList("user0", "user1");

        when(gameService.initGame(playerUserIds, playerUsernames, 3, 3)).thenReturn(mockedGame);

        Map<String, Object> gameCreationDTO = Map.of(
            "playerUserIds", playerUserIds,
            "playerUsernames", playerUsernames,
            "boardSize", 3,
            "winLength", 3
        );

        graphQlTester.document("""
                    mutation CreateGame($gameCreationDTO: GameCreationDTO!) {
                        createGame(gameCreationDTO: $gameCreationDTO) {
                            id
                        }
                    }
                """)
                .variable("gameCreationDTO", gameCreationDTO)
                .execute()
                .path("createGame.id")
                .entity(String.class)
                .isEqualTo("g0");

        verify(gameService).initGame(playerUserIds, playerUsernames, 3, 3);
    }

    @Test
    void testGetGameById() throws Exception {
        mockedGame = new Game();
        mockedGame.setId("g0");
        mockedGame.setSize(3);

        when(gameService.getGameById("g0")).thenReturn(mockedGame);

        graphQlTester.document("""
                    query QueryGameById($id: ID!) {
                        gameById(id: $id) {
                            id
                            size
                        }
                    }
                """)
                .variable("id", "g0")
                .execute()
                .path("gameById.size")
                .entity(Integer.class)
                .isEqualTo(3);

        verify(gameService).getGameById("g0");
    }

    @Test
    void testGetGamesByPlayerUsername() throws Exception {
        mockedGame = new Game();
        mockedGame.setId("g0");
        mockedGame.setSize(3);

        when(gameService.getGamesByPlayerUsername("user0")).thenReturn(Arrays.asList(mockedGame));

        graphQlTester.document("""
                    query QueryGamesByPlayerUsername($username: String!) {
                        gamesByPlayerUsername(username: $username) {
                            id
                            size
                        }
                    }
                """)
                .variable("username", "user0")
                .execute()
                .path("gamesByPlayerUsername[0].size")
                .entity(Integer.class)
                .isEqualTo(3);

        verify(gameService).getGamesByPlayerUsername("user0");
    }

    @Test
    void testPlayerMove() throws Exception {
        mockedMoveStatus = new MoveStatus(true, Mark.CROSS, null, false);

        when(gameService.doMove("g0", 0, "u0")).thenReturn(mockedMoveStatus);

        Map<String, Object> moveDTO = Map.of(
            "gameId", "g0",
            "index", 0,
            "userId", "u0"
        );

        graphQlTester.document("""
                    mutation CommitPlayerMove($moveDTO: MoveDTO!) {
                        playerMove(moveDTO: $moveDTO) {
                            isValid
                            mark
                            winner {
                                username
                            }
                            isOver
                        }
                    }
                """)
                .variable("moveDTO", moveDTO)
                .execute()
                .path("playerMove")
                .entity(MoveStatus.class)
                .isEqualTo(mockedMoveStatus);

        verify(gameService).doMove("g0", 0, "u0");
    }
}
