package id.ac.ui.cs.friendservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import id.ac.ui.cs.friendservice.model.Friend;
import id.ac.ui.cs.friendservice.model.dto.RequestApprovalDTO;
import id.ac.ui.cs.friendservice.model.dto.RequestFriendDTO;
import id.ac.ui.cs.friendservice.model.dto.UserIdListDTO;
import id.ac.ui.cs.friendservice.repository.FriendRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class FriendServiceImpl implements FriendService {

    @Autowired
    FriendRepository friendRepository;

    @Override
    public boolean exist(String user1, String user2) {
        var friend = newFriendWithOrder(user1, user2);
        return friendRepository.existsFriendByUser1IdAndUser2Id(friend.getUser1Id(), friend.getUser2Id());
    }

    @Override
    public Friend saveOrdered(String user1, String user2) {
        var friend = newFriendWithOrder(user1, user2);
        if (!exist(friend.getUser1Id(), friend.getUser2Id()))
            return friendRepository.save(friend);
        return null;
    }

    @Override
    public RequestApprovalDTO removeOrdered(RequestFriendDTO request) {
        var ordered = newFriendWithOrder(request.getReceiverId(), request.getSenderId());
        var friend = friendRepository.getFriendByUser1IdAndUser2Id(ordered.getUser1Id(), ordered.getUser2Id());
        friendRepository.delete(friend);
        return new RequestApprovalDTO("Success", "Friend is removed");
    }

    @Override
    public UserIdListDTO getFriends(String user) {
        List<String> result = new ArrayList<>();
        for (Friend friend : friendRepository.findByUser2Id(user)) {
            result.add(friend.getUser1Id());
        }
        for (Friend friend : friendRepository.findByUser1Id(user)) {
            result.add(friend.getUser2Id());
        }
        return new UserIdListDTO(result);
    }


    private Friend newFriendWithOrder(String user1, String user2) {
        var friend = new Friend();
        if (user1.compareTo(user2) > 0) {
            friend.setUser1Id(user2);
            friend.setUser2Id(user1);
        }
        else {
            friend.setUser1Id(user1);
            friend.setUser2Id(user2);
        }
        return friend;
    }
}
