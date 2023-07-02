package id.ac.ui.cs.friendservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import id.ac.ui.cs.friendservice.model.FriendRequest;
import id.ac.ui.cs.friendservice.model.dto.RequestApprovalDTO;
import id.ac.ui.cs.friendservice.model.dto.RequestFriendDTO;
import id.ac.ui.cs.friendservice.model.dto.UserIdListDTO;
import id.ac.ui.cs.friendservice.repository.FriendRequestRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class FriendRequestServiceImpl implements FriendRequestService {

    @Autowired
    FriendRequestRepository friendRequestRepository;

    @Autowired
    FriendService friendService;

    @Override
    public boolean exist(String sender, String receiver) {
        return friendRequestRepository.existsFriendRequestBySenderIdAndReceiverId(sender, receiver);
    }

    @Override
    public boolean existTwoWays(String user1, String user2) {
        return friendRequestRepository.existsFriendRequestBySenderIdAndReceiverId(user1, user2) ||
                friendRequestRepository.existsFriendRequestBySenderIdAndReceiverId(user2, user1);
    }

    @Override
    public RequestApprovalDTO request(RequestFriendDTO requestFriendDTO) {
        String receiver = requestFriendDTO.getReceiverId();
        String sender = requestFriendDTO.getSenderId();

        var opposite = friendRequestRepository.getBySenderIdAndReceiverId(receiver, sender);
        if (opposite != null) {
            // Upgrade from friend request to friend
            friendRequestRepository.delete(opposite);
            friendService.saveOrdered(receiver, sender);
            return new RequestApprovalDTO("Accepted", "Is now friends");
        } else {
            friendRequestRepository.save(new FriendRequest(sender, receiver));
            return new RequestApprovalDTO("Success", "Friend request successful");
        }
    }

    @Override
    public RequestApprovalDTO remove(RequestFriendDTO requestFriendDTO) {
        String receiver = requestFriendDTO.getReceiverId();
        String sender = requestFriendDTO.getSenderId();

        var firstWay = friendRequestRepository.getBySenderIdAndReceiverId(sender, receiver);
        if (firstWay != null) {
            friendRequestRepository.delete(firstWay);
        }
        else {
            var secondWay = friendRequestRepository.getBySenderIdAndReceiverId(receiver, sender);
            if (secondWay != null) friendRequestRepository.delete(secondWay);
        }


        return new RequestApprovalDTO("Success", "Friend request is removed");
    }

    @Override
    public UserIdListDTO getOutgoingFriendRequests(String user) {
        List<String> result = new ArrayList<>();
        for (FriendRequest request : friendRequestRepository.findBySenderId(user))
            result.add(request.getReceiverId());
        return new UserIdListDTO(result);
    }

    @Override
    public UserIdListDTO getIncomingFriendRequests(String user) {
        List<String> result = new ArrayList<>();
        for (FriendRequest request : friendRequestRepository.findByReceiverId(user))
            result.add(request.getSenderId());
        return new UserIdListDTO(result);
    }
}
