package id.ac.ui.cs.friendservice.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import id.ac.ui.cs.friendservice.model.FriendRequest;
import id.ac.ui.cs.friendservice.model.dto.UserIdListDTO;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FriendRequestTest {

    private FriendRequest friendRequest;

    @BeforeEach
    public void setUp() {
        friendRequest = new FriendRequest("a","b");
    }

    @Test
    void testEquals() {
        assertTrue(friendRequest.equals(friendRequest));
    }

    @Test
    void testHashCode() {
        assertEquals(friendRequest.hashCode(), new FriendRequest("a","b").hashCode());
    }

    @Test
    void getRequestId() {
        assertNull(friendRequest.getRequestId());
    }

    @Test
    void getSenderId() {
        assertEquals("a", friendRequest.getSenderId());
    }

    @Test
    void getReceiverId() {
        assertEquals("b", friendRequest.getReceiverId());
    }

    @Test
    void setRequestId() {
        friendRequest.setRequestId(10L);
        assertEquals(10L, friendRequest.getRequestId());
    }

    @Test
    void setSenderId() {
        friendRequest.setSenderId("c");
        assertEquals("c", friendRequest.getSenderId());
    }

    @Test
    void setReceiverId() {
        friendRequest.setReceiverId("c");
        assertEquals("c", friendRequest.getReceiverId());
    }

    @Test
    void testToString() {
        assertEquals("FriendRequest(requestId=null, senderId=a, receiverId=b)", friendRequest.toString());
    }
}