package id.ac.ui.cs.friendservice.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import id.ac.ui.cs.friendservice.model.Friend;
import id.ac.ui.cs.friendservice.model.dto.RequestApprovalDTO;
import id.ac.ui.cs.friendservice.model.dto.RequestFriendDTO;
import id.ac.ui.cs.friendservice.model.dto.UserIdListDTO;
import id.ac.ui.cs.friendservice.repository.FriendRepository;
import id.ac.ui.cs.friendservice.service.FriendServiceImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FriendServiceImplTest {

    @Mock
    FriendRepository friendRepository;

    @InjectMocks
    FriendServiceImpl friendService;

    @Test
    void exist() {
        friendService.exist("BBB", "AAA");
        verify(friendRepository).existsFriendByUser1IdAndUser2Id("AAA","BBB");
    }

    @Test
    void saveOrderedSaved() {
        Friend friend = new Friend();
        friend.setUser2Id("C");
        friend.setUser1Id("B");
        when(friendRepository.existsFriendByUser1IdAndUser2Id("B","C")).thenReturn(false);
        when(friendRepository.save(ArgumentMatchers.refEq(friend))).thenReturn(friend);
        Friend result = friendService.saveOrdered("C","B");
        assertEquals(result.getUser1Id(), friend.getUser1Id());
        assertEquals(result.getUser2Id(), friend.getUser2Id());
    }

    @Test
    void saveOrderedAlreadyExist() {
        when(friendRepository.existsFriendByUser1IdAndUser2Id("B","C")).thenReturn(true);
        Friend result = friendService.saveOrdered("C","B");
        verifyNoMoreInteractions(friendRepository);
        assertNull(result);
    }

    @Test
    void removeOrdered() {
        Friend friend = new Friend();
        friend.setUser2Id("B");
        friend.setUser1Id("A");
        when(friendRepository.getFriendByUser1IdAndUser2Id("A","B")).thenReturn(friend);
        RequestApprovalDTO approval = friendService.removeOrdered(new RequestFriendDTO("B", "A"));
        verify(friendRepository).delete(friend);
        assertEquals("Success", approval.getStatus());
        assertEquals("Friend is removed", approval.getMessage());
    }

    @Test
    void getFriends() {
        Friend d = new Friend();
        Friend c = new Friend();
        Friend a = new Friend();
        d.setUser2Id("d");
        d.setUser1Id("b");
        c.setUser2Id("c");
        c.setUser1Id("b");
        a.setUser1Id("a");
        a.setUser2Id("b");
        List<Friend> byId1 = List.of(d,c);
        List<Friend> byId2 = List.of(a);
        when(friendRepository.findByUser1Id("b")).thenReturn(byId1);
        when(friendRepository.findByUser2Id("b")).thenReturn(byId2);
        UserIdListDTO result = friendService.getFriends("b");
        verify(friendRepository).findByUser2Id("b");
        verify(friendRepository).findByUser1Id("b");
        assertEquals(3, result.getUserIds().size());
    }
}