package id.ac.ui.cs.friendservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatcher;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import id.ac.ui.cs.friendservice.model.FriendRequest;
import id.ac.ui.cs.friendservice.model.dto.RequestApprovalDTO;
import id.ac.ui.cs.friendservice.model.dto.RequestFriendDTO;
import id.ac.ui.cs.friendservice.model.dto.UserIdListDTO;
import id.ac.ui.cs.friendservice.repository.FriendRequestRepository;
import id.ac.ui.cs.friendservice.service.FriendRequestServiceImpl;
import id.ac.ui.cs.friendservice.service.FriendService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class FriendRequestServiceImplTest {

    @Mock
    FriendRequestRepository friendRequestRepository;

    @Mock
    FriendService friendService;

    @InjectMocks
    FriendRequestServiceImpl friendRequestService;

    @Test
    void exist() {
        friendRequestService.exist("A", "B");
        verify(friendRequestRepository, times(1)).existsFriendRequestBySenderIdAndReceiverId("A","B");
    }

    @Test
    void existTwoWays() {
        friendRequestService.existTwoWays("A","B");
        lenient().when(friendRequestRepository.existsFriendRequestBySenderIdAndReceiverId("A","B")).thenReturn(false);
        verify(friendRequestRepository, times(1)).existsFriendRequestBySenderIdAndReceiverId("A","B");
        verify(friendRequestRepository, times(1)).existsFriendRequestBySenderIdAndReceiverId("B","A");
    }

    @Test
    void requestSuccess() {
        FriendRequest friendRequest = new FriendRequest("A", "B");
        when(friendRequestRepository.getBySenderIdAndReceiverId("B","A")).thenReturn(null);
        RequestApprovalDTO response = friendRequestService.request(new RequestFriendDTO("A","B"));
        verify(friendRequestRepository).getBySenderIdAndReceiverId("B","A");
        verify(friendRequestRepository).save(ArgumentMatchers.refEq(friendRequest));
        assertEquals("Success", response.getStatus());
        assertEquals("Friend request successful", response.getMessage());
    }

    @Test
    void requestAccepted() {
        FriendRequest friendRequest = new FriendRequest("A", "B");
        when(friendRequestRepository.getBySenderIdAndReceiverId("B","A")).thenReturn(friendRequest);
        RequestApprovalDTO response = friendRequestService.request(new RequestFriendDTO("A","B"));
        verify(friendRequestRepository).getBySenderIdAndReceiverId("B","A");
        verify(friendRequestRepository).delete(friendRequest);
        verify(friendService).saveOrdered("B", "A");
        assertEquals("Accepted", response.getStatus());
        assertEquals("Is now friends", response.getMessage());
    }

    @Test
    void removeNoFriendRequestFound() {
        when(friendRequestRepository.getBySenderIdAndReceiverId("A","B")).thenReturn(null);
        when(friendRequestRepository.getBySenderIdAndReceiverId("B","A")).thenReturn(null);
        friendRequestService.remove(new RequestFriendDTO("A","B"));
        verify(friendRequestRepository,times(0)).delete(any());
    }

    @Test
    void removeFriendRequestFoundFirstWay() {
        FriendRequest firstWay = new FriendRequest("A","B");
        when(friendRequestRepository.getBySenderIdAndReceiverId("A","B")).thenReturn(firstWay);
        friendRequestService.remove(new RequestFriendDTO("A","B"));
        verify(friendRequestRepository).delete(firstWay);
    }
    @Test
    void removeFriendRequestFoundSecondWay() {
        FriendRequest secondWay = new FriendRequest("B","A");
        when(friendRequestRepository.getBySenderIdAndReceiverId("A","B")).thenReturn(null);
        when(friendRequestRepository.getBySenderIdAndReceiverId("B","A")).thenReturn(secondWay);
        friendRequestService.remove(new RequestFriendDTO("A","B"));
        verify(friendRequestRepository).delete(secondWay);
    }

    @Test
    void getOutgoingFriendRequests() {
        List<FriendRequest> friendRequestsOfA = List.of(new FriendRequest("A","C"), new FriendRequest("A","B"));
        when(friendRequestRepository.findBySenderId("A")).thenReturn(friendRequestsOfA);

        UserIdListDTO result = friendRequestService.getOutgoingFriendRequests("A");
        verify(friendRequestRepository, times(1)).findBySenderId("A");
        assertEquals(2, result.getUserIds().size());
    }

    @Test
    void getIncomingFriendRequests() {
        List<FriendRequest> friendRequestsOfC = List.of(new FriendRequest("A","C"), new FriendRequest("B","C"));
        when(friendRequestRepository.findByReceiverId("C")).thenReturn(friendRequestsOfC);

        UserIdListDTO result = friendRequestService.getIncomingFriendRequests("C");
        verify(friendRequestRepository, times(1)).findByReceiverId("C");
        assertEquals(2, result.getUserIds().size());
    }
}