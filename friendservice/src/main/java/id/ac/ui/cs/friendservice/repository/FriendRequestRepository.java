package id.ac.ui.cs.friendservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import id.ac.ui.cs.friendservice.model.FriendRequest;

import java.util.List;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Integer> {

    boolean existsFriendRequestBySenderIdAndReceiverId(String senderId, String receiverId);
    FriendRequest getBySenderIdAndReceiverId(String senderId, String receiverId);
    List<FriendRequest> findByReceiverId(String receiverId);
    List<FriendRequest> findBySenderId(String senderId);

}
