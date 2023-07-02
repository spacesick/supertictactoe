package id.ac.ui.cs.supertictactoe.service;

import java.util.List;

import id.ac.ui.cs.supertictactoe.model.User;
import id.ac.ui.cs.supertictactoe.model.dto.RequestFriendDTO;


public interface FriendService {

    String requestFriendById(String senderId, String receiverId);
    String requestFriendByUsername(String senderId, String receiverUsername);
    List<User> listFriends(String userId);
    List<User> listOutgoingFriendRequest(String userId);
    List<User> listIncomingFriendRequest(String userId);
    String removeFriendRequest(RequestFriendDTO requestFriendDTO);
    String removeFriend(RequestFriendDTO requestFriendDTO);
}
