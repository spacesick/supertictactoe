package id.ac.ui.cs.supertictactoe.controller;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import id.ac.ui.cs.supertictactoe.controller.GameController;
import id.ac.ui.cs.supertictactoe.exception.ForbiddenGameSettingsException;
import id.ac.ui.cs.supertictactoe.exception.GameNotFoundException;
import id.ac.ui.cs.supertictactoe.exception.RoomNotFoundException;
import id.ac.ui.cs.supertictactoe.exception.UnwinnableGameException;
import id.ac.ui.cs.supertictactoe.model.Game;
import id.ac.ui.cs.supertictactoe.model.Mark;
import id.ac.ui.cs.supertictactoe.model.MoveStatus;
import id.ac.ui.cs.supertictactoe.model.Player;
import id.ac.ui.cs.supertictactoe.model.User;
import id.ac.ui.cs.supertictactoe.service.GameService;

@SpringBootTest
@ContextConfiguration
@WebAppConfiguration
class GameControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Mock
    private Game mockedGame;

    @Mock
    private MoveStatus mockedMoveStatus;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private SimpMessagingTemplate template;

    @MockBean
    private GameService gameService;

    private User mockedAppUser;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
                
        mockedAppUser = new User();
        mockedAppUser.setUserId("u0");
    }

    @Test
    void whenPlayNewGameAndIsValidShouldRedirect() throws Exception {
        String gameId = "g0";
        when(gameService.initGame("r0", 3, 3)).thenReturn(gameId);

        mockMvc.perform(
                get("/{roomId}/play", "r0")
                        .param("b", "3")
                        .param("w", "3")
                        .with(user(mockedAppUser)))
                .andExpect((handler().methodName("playNewGame")))
                .andExpect(model().attributeDoesNotExist("entity"))
                .andExpect(view().name("redirect:/games/g0"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/games/g0"));

        verify(gameService).initGame("r0", 3, 3);
    }

    @Test
    void whenPlayNewGameAndRoomNotFoundShouldHandleException() throws Exception {
        when(gameService.initGame("r1", 3, 3)).thenThrow(new RoomNotFoundException());

        mockMvc.perform(
                get("/{roomId}/play", "r1")
                        .param("b", "3")
                        .param("w", "3")
                        .with(user(mockedAppUser)))
                .andExpect((handler().methodName("playNewGame")))
                .andExpect(model().attributeExists("message"))
                .andExpect(view().name("error_page"))
                .andExpect(status().isOk());
    }

    @Test
    void whenPlayNewGameAndIsUnwinnableShouldHandleException() throws Exception {
        when(gameService.initGame("r0", 3, 5)).thenThrow(new UnwinnableGameException());

        mockMvc.perform(
                get("/{roomId}/play", "r0")
                        .param("b", "3")
                        .param("w", "5")
                        .with(user(mockedAppUser)))
                .andExpect((handler().methodName("playNewGame")))
                .andExpect(model().attributeExists("message"))
                .andExpect(view().name("error_page"))
                .andExpect(status().isOk());
    }

    @Test
    void whenPlayNewGameAndSettingsAreForbiddenShouldHandleException() throws Exception {
        when(gameService.initGame("r0", 0, -1)).thenThrow(new ForbiddenGameSettingsException());

        mockMvc.perform(
                get("/{roomId}/play", "r0")
                        .param("b", "0")
                        .param("w", "-1")
                        .with(user(mockedAppUser)))
                .andExpect((handler().methodName("playNewGame")))
                .andExpect(model().attributeExists("message"))
                .andExpect(view().name("error_page"))
                .andExpect(status().isOk());
    }

    @Test
    void whenPlayNewGameAndInvalidParamsShouldHandleException() throws Exception {
        String gameId = "g0";
        when(gameService.initGame("r0", 3, 3)).thenReturn(gameId);

        mockMvc.perform(
                get("/{roomId}/play", "r0")
                        .param("b", "x")
                        .param("w", "y")
                        .with(user(mockedAppUser)))
                .andExpect((handler().methodName("playNewGame")))
                .andExpect(model().attributeExists("message"))
                .andExpect(view().name("error_page"))
                .andExpect(status().isOk());
    }

    @Test
    void whenGetGamePageAndValidShouldReturnGamePage() throws Exception {
        List<Mark> initMarkings = new ArrayList<>(Arrays.asList(new Mark[3 * 3]));
        Collections.fill(initMarkings, Mark.NONE);

        mockedGame = new Game("g0", initMarkings, 3, 3, new ArrayList<>(), 0, -1, false);
        when(gameService.getGameById("g0")).thenReturn(mockedGame);

        mockMvc.perform(
                get("/games/{gameId}", "g0")
                        .with(user(mockedAppUser)))
                .andExpect((handler().methodName("gamePage")))
                .andExpect(model().attributeExists("game", "userId"))
                .andExpect(model().attributeDoesNotExist("entity"))
                .andExpect(view().name("game_page"))
                .andExpect(status().isOk());
    }

    @Test
    void whenGetGamePageAndGameNotFoundShouldHandleException() throws Exception {
        when(gameService.getGameById("g1")).thenThrow(new GameNotFoundException());

        mockMvc.perform(
                get("/games/{gameId}", "g1")
                        .with(user(mockedAppUser)))
                .andExpect((handler().methodName("gamePage")))
                .andExpect(model().attributeExists("message"))
                .andExpect(model().attributeDoesNotExist("game", "userId"))
                .andExpect(view().name("error_page"))
                .andExpect(status().isOk());
    }

    @Test
    void whenPostPlayerMoveAndValidShouldReturnMoveStatus() throws Exception {
        mockedMoveStatus = new MoveStatus(true, Mark.NOUGHT, new Player(), false);
        when(gameService.doMove("g0", 0, mockedAppUser.getUserId())).thenReturn(mockedMoveStatus);

        mockMvc.perform(
                post("/service/{gameId}/move", "g0")
                        .param("pos", "0")
                        .with(user(mockedAppUser)))
                .andExpect((handler().methodName("playerMove")))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.valid").value(true))
                .andExpect(jsonPath("$.mark").value(Mark.NOUGHT.toString()))
                .andExpect(jsonPath("$.over").value(false))
                .andExpect(status().isOk());

        verify(gameService).doMove("g0", 0, mockedAppUser.getUserId());
    }

    @Test
    void whenPostPlayerMoveAndInvalidParamShouldReturnMoveStatus() throws Exception {
        mockedMoveStatus = new MoveStatus(true, Mark.NOUGHT, new Player(), false);
        when(gameService.doMove("g0", 0, mockedAppUser.getUserId())).thenReturn(mockedMoveStatus);

        mockMvc.perform(
                post("/service/{gameId}/move", "g0")
                        .param("pos", "x")
                        .with(user(mockedAppUser)))
                .andExpect((handler().methodName("playerMove")))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.valid").value(false))
                .andExpect(jsonPath("$.mark").value(Mark.NONE.toString()))
                .andExpect(jsonPath("$.over").value(false))
                .andExpect(status().isOk());

        verify(gameService, times(0)).doMove("g0", 0, mockedAppUser.getUserId());
    }
}
