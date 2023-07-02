package id.ac.ui.cs.supertictactoe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import id.ac.ui.cs.supertictactoe.model.User;
import id.ac.ui.cs.supertictactoe.model.dto.RequestFriendDTO;
import id.ac.ui.cs.supertictactoe.repository.UserRepository;
import id.ac.ui.cs.supertictactoe.vo.RequestApproval;
import id.ac.ui.cs.supertictactoe.vo.UserIdList;

import java.util.List;
import java.util.Objects;

@Service
public class FriendServiceImpl implements FriendService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RestTemplate restTemplate;

    @Value("${friend.service.restapi.url}")
    private String FRIEND_URL;
    
    private static final String FAILED = "Failed";
    private static final String SUCCESS = "Success";
    private static final String ACCEPTED = "Accepted";

    @Override
    public final String requestFriendByUsername(final String senderId, final String receiverUsername) {
        User user = userRepository.findByUsername(receiverUsername);
        if (user == null) {
            return "User not found.";
        }
        return requestFriend(new RequestFriendDTO(senderId, user.getUserId()));
    }

    @Override
    public final String requestFriendById(final String senderId, final String receiverId) {
        if (!userRepository.existsById(receiverId)) {
            return "User not found.";
        }
        return requestFriend(new RequestFriendDTO(senderId, receiverId));
    }

    final String requestFriend(final RequestFriendDTO request) {
        ResponseEntity<RequestApproval> requestApproval = restTemplate.postForEntity(FRIEND_URL + "/request", request, RequestApproval.class);
        String status = Objects.requireNonNull(requestApproval.getBody()).getStatus();

        if (FAILED.equals(status)) {
            return Objects.requireNonNull(requestApproval.getBody()).getMessage();
        } else if (SUCCESS.equals(status)) {
            return "Friend request sent.";
        } else if (ACCEPTED.equals(status)) {
            return "Friend request accepted.";
        } else {
            return null;
        }
    }


    @Override
    public final List<User> listFriends(final String userId) {
        ResponseEntity<UserIdList> response = restTemplate.getForEntity(FRIEND_URL + "/friend/?userId={userId}", UserIdList.class, userId);
        List<String> userIds = Objects.requireNonNull(response.getBody()).getUserIds();
        return userRepository.findAllByUserIdIsIn(userIds);
    }

    @Override
    public final List<User> listOutgoingFriendRequest(final String userId) {
        ResponseEntity<UserIdList> response = restTemplate.getForEntity(FRIEND_URL + "/outgoing-request/?userId={userId}", UserIdList.class, userId);
        List<String> userIds = Objects.requireNonNull(response.getBody()).getUserIds();
        return userRepository.findAllByUserIdIsIn(userIds);
    }

    @Override
    public final List<User> listIncomingFriendRequest(final String userId) {
        ResponseEntity<UserIdList> response = restTemplate.getForEntity(FRIEND_URL + "/incoming-request/?userId={userId}", UserIdList.class, userId);
        List<String> userIds = Objects.requireNonNull(response.getBody()).getUserIds();
        return userRepository.findAllByUserIdIsIn(userIds);
    }

    @Override
    public final String removeFriendRequest(final RequestFriendDTO request) {
        ResponseEntity<RequestApproval> requestApproval = restTemplate.postForEntity(FRIEND_URL + "/request-remove", request, RequestApproval.class);
        String status = Objects.requireNonNull(requestApproval.getBody()).getStatus();
        if (FAILED.equals(status)) {
            return "Remove failed, please try again";
        } else if (SUCCESS.equals(status)) {
            return "Friend request is successfully removed";
        }
        else {
            return null;
        }
    }

    @Override
    public final String removeFriend(final RequestFriendDTO request) {
        ResponseEntity<RequestApproval> requestApproval = restTemplate.postForEntity(FRIEND_URL + "/friend-remove", request, RequestApproval.class);
        String status = Objects.requireNonNull(requestApproval.getBody()).getStatus();
        if (FAILED.equals(status)) {
            return "Remove failed, please try again";
        } else if (SUCCESS.equals(status)) {
            return "Friend is successfully removed";
        }
        else {
            return null;
        }
    }
}
