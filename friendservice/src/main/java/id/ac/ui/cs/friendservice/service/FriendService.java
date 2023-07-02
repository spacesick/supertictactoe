package id.ac.ui.cs.friendservice.service;

import id.ac.ui.cs.friendservice.model.Friend;
import id.ac.ui.cs.friendservice.model.dto.RequestApprovalDTO;
import id.ac.ui.cs.friendservice.model.dto.RequestFriendDTO;
import id.ac.ui.cs.friendservice.model.dto.UserIdListDTO;

public interface FriendService {
    boolean exist(String user1, String user2);
    Friend saveOrdered(String user1, String user2);
    RequestApprovalDTO removeOrdered(RequestFriendDTO request);
    UserIdListDTO getFriends(String user);
}