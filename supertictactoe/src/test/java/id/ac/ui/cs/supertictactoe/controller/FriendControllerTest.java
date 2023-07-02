package id.ac.ui.cs.supertictactoe.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import id.ac.ui.cs.supertictactoe.model.User;
import id.ac.ui.cs.supertictactoe.model.dto.RequestFriendDTO;
import id.ac.ui.cs.supertictactoe.service.FriendService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ContextConfiguration
@WebAppConfiguration
class FriendControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;
    
    private MockMvc mockMvc;

    @MockBean
    private FriendService friendService;

    private User userApp;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();

        userApp = new User();
        userApp.setUserId("id");
    }

    @Test
    void addUser() throws Exception {
        when(friendService.requestFriendByUsername("id", "username")).thenReturn("good");
        mockMvc.perform(post("/friend/add")
                        .param("friendUsername", "username")
                        .with(user(userApp)))
                .andExpect(handler().methodName("addUser"))
                .andExpect(flash().attribute("feedback", "good"))
                .andExpect(redirectedUrl("list"));
    }

    @Test
    void acceptUser() throws Exception {
        when(friendService.requestFriendById("id", "id2")).thenReturn("good");
        mockMvc.perform(post("/friend/accept")
                        .param("friendId", "id2")
                        .with(user(userApp)))
                .andExpect(handler().methodName("acceptUser"))
                .andExpect(flash().attribute("feedback", "good"))
                .andExpect(redirectedUrl("list"));
    }

    @Test
    void ignoreRequest() throws Exception {
        when(friendService.removeFriendRequest(new RequestFriendDTO("id2", "id"))).thenReturn("good");
        mockMvc.perform(post("/friend/ignore-request")
                        .param("friendId", "id2")
                        .with(user(userApp)))
                .andExpect(handler().methodName("ignoreRequest"))
                .andExpect(flash().attribute("feedback", "good"))
                .andExpect(redirectedUrl("list"));
    }

    @Test
    void removeRequest() throws Exception {
        when(friendService.removeFriendRequest(new RequestFriendDTO("id", "id2"))).thenReturn("good");
        mockMvc.perform(post("/friend/remove-request")
                        .param("friendId", "id2")
                        .with(user(userApp)))
                .andExpect(handler().methodName("removeRequest"))
                .andExpect(flash().attribute("feedback", "good"))
                .andExpect(redirectedUrl("list"));
    }

    @Test
    void removeFriend() throws Exception {
        when(friendService.removeFriend(new RequestFriendDTO("id", "id2"))).thenReturn("good");
        mockMvc.perform(post("/friend/remove-friend")
                        .param("friendId", "id2")
                        .with(user(userApp)))
                .andExpect(handler().methodName("removeFriend"))
                .andExpect(flash().attribute("feedback", "good"))
                .andExpect(redirectedUrl("list"));
    }

    @Test
    void listFriend() throws Exception {
        List<User> userList = new ArrayList<>();
        userList.add(new User());
        when(friendService.listFriends("id")).thenReturn(userList);
        when(friendService.listIncomingFriendRequest("id")).thenReturn(userList);
        when(friendService.listOutgoingFriendRequest("id")).thenReturn(userList);
        mockMvc.perform(get("/friend/list")
                        .param("feedback", "good")
                        .with(user(userApp)))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("listFriend"))
                .andExpect(model().attribute("current", "id"))
                .andExpect(model().attribute("friends", userList))
                .andExpect(model().attribute("incoming", userList))
                .andExpect(model().attribute("outgoing", userList))
                .andExpect(model().attribute("feedback", "good"))
                .andExpect(view().name("friend"));
    }
}