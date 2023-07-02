package id.ac.ui.cs.supertictactoe.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import id.ac.ui.cs.supertictactoe.model.Room;
import id.ac.ui.cs.supertictactoe.model.User;
import id.ac.ui.cs.supertictactoe.model.dto.UserProfileDTO;
import id.ac.ui.cs.supertictactoe.service.RoomService;
import id.ac.ui.cs.supertictactoe.service.UserService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ContextConfiguration
@WebAppConfiguration
class RoomsControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private RoomService roomService;


    private User mockedAppUser1;


    private User mockedAppUser2;


    private Room mockedRoom;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();

        mockedAppUser1 = new User();
        mockedAppUser2 = new User();
        mockedRoom = new Room();

        mockedAppUser1.setUserId("u1");
        mockedAppUser1.setUsername("username");
        mockedAppUser1.setEmail("email");
        mockedAppUser1.setFirstName("name");
        mockedAppUser1.setLastName("last");
        mockedAppUser1.setPassword("password");
        mockedAppUser2.setUserId("u2");
        mockedAppUser2.setUsername("username2");
        mockedAppUser2.setEmail("email2");
        mockedAppUser2.setFirstName("name2");
        mockedAppUser2.setLastName("last2");
        mockedAppUser2.setPassword("password2");
        mockedRoom.setRoomId("r0");
    }

    @Test
    void whenGetRoomListShouldReturnRoomListPage() throws Exception {
        List mockList = new ArrayList();
        when(roomService.getRoomList()).thenReturn(mockList);

        mockMvc.perform(get("/rooms")
                .with(user(mockedAppUser1)))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("list"))
                .andExpect(model().attribute("rooms", mockList))
                .andExpect(view().name("rooms"));
    }
    @Test
    void whenGetCreateRoomShouldReturnCreateRoomPage() throws Exception {
        when(roomService.createRoom()).thenReturn(mockedRoom);

        mockMvc.perform(get("/rooms/create")
                .with(user(mockedAppUser1)))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("create"))
                .andExpect(model().attribute("roomName", mockedRoom))
                .andExpect(view().name("create_room"));
    }

    @Test
    void whenPostCreateRoomShouldReturnCreateRoomPage() throws Exception {
        mockedRoom.setRoomName("roomTest");
        when(roomService.createRoom("roomTest")).thenReturn(mockedRoom);

        mockMvc.perform(post("/rooms/create")
                        .param("roomName", "roomTest")
                        .with(user(mockedAppUser1)))
                .andExpect(handler().methodName("create"))
                .andExpect(view().name("redirect:/rooms/join/r0"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rooms/join/r0"));
    }

    @Test
    void whenJoinRoomShouldReturnJoinRoomPage() throws Exception {
        mockedRoom.setRoomName("roomTest");
        mockedRoom.setPlayer1(mockedAppUser1);
        when(roomService.joinRoom("u1","r0")).thenReturn(mockedRoom);

        mockMvc.perform(get("/rooms/join/{roomId}","r0")
                        .with(user(mockedAppUser1)))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("join"))
                .andExpect(model().attribute("room", mockedRoom))
                .andExpect(model().attribute("userId", "u1"))
                .andExpect(view().name("join_room"));
    }
}
