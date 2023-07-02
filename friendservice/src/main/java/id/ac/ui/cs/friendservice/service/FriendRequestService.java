package id.ac.ui.cs.friendservice.service;

import id.ac.ui.cs.friendservice.model.dto.RequestApprovalDTO;
import id.ac.ui.cs.friendservice.model.dto.RequestFriendDTO;
import id.ac.ui.cs.friendservice.model.dto.UserIdListDTO;

public interface FriendRequestService {
    boolean exist(String sender, String receiver);
    boolean existTwoWays(String user1, String user2);
    RequestApprovalDTO request(RequestFriendDTO requestFriendDTO);
    RequestApprovalDTO remove(RequestFriendDTO requestFriendDTO);
    UserIdListDTO getOutgoingFriendRequests(String user);
    UserIdListDTO getIncomingFriendRequests(String user);
}
