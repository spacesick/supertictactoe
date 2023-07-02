package id.ac.ui.cs.supertictactoe.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import id.ac.ui.cs.supertictactoe.model.dto.RequestFriendDTO;
import id.ac.ui.cs.supertictactoe.repository.UserRepository;
import id.ac.ui.cs.supertictactoe.vo.RequestApproval;
import id.ac.ui.cs.supertictactoe.vo.UserIdList;

import java.util.List;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class FriendServiceImplTest {

    @InjectMocks
    @Spy
    FriendServiceImpl friendService;

    @Mock
    UserRepository userRepository;

    @Mock
    RestTemplate restTemplate;

    @Value("${friend.service.restapi.url}")
    private String FRIEND_URL;

    @Test
    void requestFriendByUsernameWithUserIsNotFound() {
        Mockito.when(userRepository.findByUsername("Username")).thenReturn(null);
        String result = friendService.requestFriendByUsername("A", "Username");
        Assertions.assertEquals("User not found.", result);
    }

    @Test
    void requestFriendByIdWithUserIsNotFound() {
        Mockito.when(userRepository.existsById("id")).thenReturn(false);
        String result = friendService.requestFriendById("A", "id");
        Assertions.assertEquals("User not found.", result);
    }

    @Test
    void requestFriendIsSuccess() {
        RequestFriendDTO request = new RequestFriendDTO("A", "B");
        RequestApproval approval = new RequestApproval("Success", "is successful");
        Mockito.when(restTemplate.postForEntity(FRIEND_URL + "/request", request, RequestApproval.class))
                .thenReturn(new ResponseEntity<>(approval, HttpStatus.OK));
        String result = friendService.requestFriend(request);
        Assertions.assertEquals("Friend request sent.", result);
    }

    @Test
    void requestFriendIsFailed() {
        RequestFriendDTO request = new RequestFriendDTO("A", "B");
        RequestApproval approval = new RequestApproval("Failed", "is failed");
        Mockito.when(restTemplate.postForEntity(FRIEND_URL + "/request", request, RequestApproval.class))
                .thenReturn(new ResponseEntity<>(approval, HttpStatus.OK));
        String result = friendService.requestFriend(request);
        Assertions.assertEquals("is failed", result);
    }

    @Test
    void requestFriendIsAccepted() {
        RequestFriendDTO request = new RequestFriendDTO("A", "B");
        RequestApproval approval = new RequestApproval("Accepted", "is accepted");
        Mockito.when(restTemplate.postForEntity(FRIEND_URL + "/request", request, RequestApproval.class))
                .thenReturn(new ResponseEntity<>(approval, HttpStatus.OK));
        String result = friendService.requestFriend(request);
        Assertions.assertEquals("Friend request accepted.", result);
    }

    @Test
    void listFriends() {
        String userId = "1";
        List<String> ids = List.of(new String[]{"2", "3", "4"});
        UserIdList response = new UserIdList(ids);
        Mockito.when(restTemplate.getForEntity(FRIEND_URL + "/friend/?userId={userId}", UserIdList.class, userId))
                .thenReturn(new ResponseEntity<>(response, HttpStatus.OK));
        friendService.listFriends(userId);
        verify(userRepository).findAllByUserIdIsIn(ids);
    }

    @Test
    void listOutgoingFriendRequest() {
        String userId = "1";
        List<String> ids = List.of(new String[]{"2", "3", "4"});
        UserIdList response = new UserIdList(ids);
        Mockito.when(restTemplate.getForEntity(FRIEND_URL + "/outgoing-request/?userId={userId}", UserIdList.class, userId))
                .thenReturn(new ResponseEntity<>(response, HttpStatus.OK));
        friendService.listOutgoingFriendRequest(userId);
        verify(userRepository).findAllByUserIdIsIn(ids);
    }

    @Test
    void listIncomingFriendRequest() {
        String userId = "1";
        List<String> ids = List.of(new String[]{"2", "3", "4"});
        UserIdList response = new UserIdList(ids);
        Mockito.when(restTemplate.getForEntity(FRIEND_URL + "/incoming-request/?userId={userId}", UserIdList.class, userId))
                .thenReturn(new ResponseEntity<>(response, HttpStatus.OK));
        friendService.listIncomingFriendRequest(userId);
        verify(userRepository).findAllByUserIdIsIn(ids);
    }

    @Test
    void removeFriendRequestIsSuccess() {
        RequestFriendDTO request = new RequestFriendDTO("A", "B");
        RequestApproval approval = new RequestApproval("Success", "is successful");
        Mockito.when(restTemplate.postForEntity(FRIEND_URL + "/request-remove", request, RequestApproval.class))
                .thenReturn(new ResponseEntity<>(approval, HttpStatus.OK));
        String result = friendService.removeFriendRequest(request);
        Assertions.assertEquals("Friend request is successfully removed", result);
    }

    @Test
    void removeFriendRequestIsFailed() {
        RequestFriendDTO request = new RequestFriendDTO("A", "B");
        RequestApproval approval = new RequestApproval("Failed", "is failed");
        Mockito.when(restTemplate.postForEntity(FRIEND_URL + "/request-remove", request, RequestApproval.class))
                .thenReturn(new ResponseEntity<>(approval, HttpStatus.OK));
        String result = friendService.removeFriendRequest(request);
        Assertions.assertEquals("Remove failed, please try again", result);
    }

    @Test
    void removeFriendIsSuccess() {
        RequestFriendDTO request = new RequestFriendDTO("A", "B");
        RequestApproval approval = new RequestApproval("Success", "is successful");
        Mockito.when(restTemplate.postForEntity(FRIEND_URL + "/friend-remove", request, RequestApproval.class))
                .thenReturn(new ResponseEntity<>(approval, HttpStatus.OK));
        String result = friendService.removeFriend(request);
        Assertions.assertEquals("Friend is successfully removed", result);
    }

    @Test
    void removeFriendIsFailed() {
        RequestFriendDTO request = new RequestFriendDTO("A", "B");
        RequestApproval approval = new RequestApproval("Failed", "is failed");
        Mockito.when(restTemplate.postForEntity(FRIEND_URL + "/friend-remove", request, RequestApproval.class))
                .thenReturn(new ResponseEntity<>(approval, HttpStatus.OK));
        String result = friendService.removeFriend(request);
        Assertions.assertEquals("Remove failed, please try again", result);
    }
}