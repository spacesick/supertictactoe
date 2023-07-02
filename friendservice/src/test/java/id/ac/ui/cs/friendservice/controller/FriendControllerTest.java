package id.ac.ui.cs.friendservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import id.ac.ui.cs.friendservice.controller.FriendController;
import id.ac.ui.cs.friendservice.model.dto.RequestApprovalDTO;
import id.ac.ui.cs.friendservice.model.dto.RequestFriendDTO;
import id.ac.ui.cs.friendservice.service.FriendRequestService;
import id.ac.ui.cs.friendservice.service.FriendService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = FriendController.class)
class FriendControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FriendRequestService friendRequestService;

    @MockBean
    private FriendService friendService;

    @Test
    void requestFriendWhenWithSelf() throws Exception {
        RequestApprovalDTO expected = new RequestApprovalDTO("Failed", "Can't be friend with themselves");
        RequestFriendDTO input = new RequestFriendDTO("a","a");
        mockMvc.perform(post("/friend/api/request")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(input)))
                .andExpect(status().isOk())
                .andExpect(content().json(asJsonString(expected)));
    }

    @Test
    void requestFriendWhenAlreadyExist() throws Exception {
        RequestApprovalDTO expected = new RequestApprovalDTO("Failed", "Is already requesting");
        RequestFriendDTO input = new RequestFriendDTO("a","b");
        when(friendRequestService.exist("a","b")).thenReturn(true);
        mockMvc.perform(post("/friend/api/request")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(input)))
                .andExpect(status().isOk())
                .andExpect(content().json(asJsonString(expected)));
    }

    @Test
    void requestFriendWhenAlreadyFriend() throws Exception {
        RequestApprovalDTO expected = new RequestApprovalDTO("Failed", "Is already a friend");
        RequestFriendDTO input = new RequestFriendDTO("a","b");
        when(friendRequestService.exist("a","b")).thenReturn(false);
        when(friendService.exist("a","b")).thenReturn(true);
        mockMvc.perform(post("/friend/api/request")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(input)))
                .andExpect(status().isOk())
                .andExpect(content().json(asJsonString(expected)));
    }

    @Test
    void requestFriendWhenSuccess() throws Exception {
        RequestApprovalDTO expected = new RequestApprovalDTO("Failed", "Is already a friend");
        RequestFriendDTO input = new RequestFriendDTO("a","b");
        when(friendRequestService.exist("a","b")).thenReturn(false);
        when(friendService.exist("a","b")).thenReturn(false);
        mockMvc.perform(post("/friend/api/request")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(input)))
                .andExpect(status().isOk());
        verify(friendRequestService).request(input);
    }


    @Test
    void removeFriendRequestWhenNotExist() throws Exception {
        RequestApprovalDTO expected = new RequestApprovalDTO("Failed", "Request doesn't exist");
        RequestFriendDTO input = new RequestFriendDTO("a","b");
        when(friendRequestService.exist("a","b")).thenReturn(false);
        mockMvc.perform(post("/friend/api/request-remove")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(input)))
                .andExpect(status().isOk())
                .andExpect(content().json(asJsonString(expected)));
    }

    @Test
    void removeFriendRequestWhenSuccessful() throws Exception {
        RequestFriendDTO input = new RequestFriendDTO("a","b");
        when(friendRequestService.exist("a","b")).thenReturn(true);
        mockMvc.perform(post("/friend/api/request-remove")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(input)))
                .andExpect(status().isOk());
        verify(friendRequestService).remove(input);
    }

    @Test
    void removeFriendWhenNotExist() throws Exception {
        RequestApprovalDTO expected = new RequestApprovalDTO("Failed", "Friend doesn't exist");
        RequestFriendDTO input = new RequestFriendDTO("a","b");
        when(friendService.exist("a","b")).thenReturn(false);
        mockMvc.perform(post("/friend/api/friend-remove")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(input)))
                .andExpect(status().isOk())
                .andExpect(content().json(asJsonString(expected)));
    }

    @Test
    void removeFriendWhenSuccessful() throws Exception {
        RequestFriendDTO input = new RequestFriendDTO("a","b");
        when(friendService.exist("a","b")).thenReturn(true);
        mockMvc.perform(post("/friend/api/friend-remove")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(input)))
                .andExpect(status().isOk());
        verify(friendService).removeOrdered(input);
    }

    @Test
    void listOutgoingFriendRequest() throws Exception {
        mockMvc.perform(get("/friend/api/outgoing-request")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("userId","a"))
                .andExpect(status().isOk());
        verify(friendRequestService).getOutgoingFriendRequests("a");
    }

    @Test
    void listIncomingFriendRequest() throws Exception{
        mockMvc.perform(get("/friend/api/incoming-request")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("userId","a"))
                .andExpect(status().isOk());
        verify(friendRequestService).getIncomingFriendRequests("a");
    }

    @Test
    void listFriend() throws Exception{
        mockMvc.perform(get("/friend/api/friend")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("userId","a"))
                .andExpect(status().isOk());
        verify(friendService).getFriends("a");
    }


    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}