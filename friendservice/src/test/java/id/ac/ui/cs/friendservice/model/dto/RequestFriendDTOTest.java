package id.ac.ui.cs.friendservice.model.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import id.ac.ui.cs.friendservice.model.dto.RequestFriendDTO;

import static org.junit.jupiter.api.Assertions.*;

class RequestFriendDTOTest {

    private RequestFriendDTO requestFriendDTO;

    @BeforeEach
    public void setUp(){
        requestFriendDTO = new RequestFriendDTO("Sender", "Receiver");
    }

    @Test
    void getSenderId() {
        assertEquals("Sender", requestFriendDTO.getSenderId());
    }

    @Test
    void getReceiverId() {
        assertEquals("Receiver", requestFriendDTO.getReceiverId());
    }

    @Test
    void setSenderId() {
        requestFriendDTO.setSenderId("New");
        assertEquals("New", requestFriendDTO.getSenderId());
    }

    @Test
    void setReceiverId() {
        requestFriendDTO.setReceiverId("New");
        assertEquals("New", requestFriendDTO.getReceiverId());
    }

    @Test
    void testEqualsCorrect() {
        assertTrue(requestFriendDTO.equals(requestFriendDTO));
    }

    @Test
    void testEqualsFalse() {
        assertFalse(requestFriendDTO.equals(new RequestFriendDTO("a","b")));
    }

    @Test
    void testCanEquals() {
        assertTrue(requestFriendDTO.canEqual(new RequestFriendDTO("a","b")));
    }

    @Test
    void testHashCode() {
        RequestFriendDTO a = new RequestFriendDTO("a","b");
        RequestFriendDTO b = new RequestFriendDTO("a","b");
        assertEquals(a.hashCode(), b.hashCode());

    }

    @Test
    void testToString() {
        assertEquals("RequestFriendDTO(senderId=Sender, receiverId=Receiver)", requestFriendDTO.toString());
    }
}