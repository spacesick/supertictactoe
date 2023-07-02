package id.ac.ui.cs.friendservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import id.ac.ui.cs.friendservice.model.Friend;

import java.util.List;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Integer> {

    boolean existsFriendByUser1IdAndUser2Id(String user1Id, String user2Id);
    Friend getFriendByUser1IdAndUser2Id(String user1Id, String user2Id);
    List<Friend> findByUser1Id(String userId);
    List<Friend> findByUser2Id(String userId);
}
