package id.ac.ui.cs.friendservice.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import id.ac.ui.cs.friendservice.model.Friend;

import static org.junit.jupiter.api.Assertions.*;

class FriendTest {

    private Friend friend;

    @BeforeEach
    public void setUp() {
        friend = new Friend();
        friend.setFriendId(1L);
        friend.setUser1Id("1");
        friend.setUser2Id("2");
    }

    @Test
    void testEquals() {
        assertTrue(friend.equals(friend));
    }

    @Test
    void testHashCode() {
        Friend other = new Friend();
        other.setFriendId(1L);
        other.setUser1Id("1");
        other.setUser2Id("2");
        assertEquals(friend.hashCode(), other.hashCode());
    }

    @Test
    void getFriendId() {
        assertEquals(1L, friend.getFriendId());
    }

    @Test
    void getUser1Id() {
        assertEquals("1", friend.getUser1Id());
    }

    @Test
    void getUser2Id() {
        assertEquals("2", friend.getUser2Id());
    }

    @Test
    void setFriendId() {
        friend.setFriendId(10L);
        assertEquals(10L, friend.getFriendId());
    }

    @Test
    void setUser1Id() {
        friend.setUser1Id("c");
        assertEquals("c", friend.getUser1Id());
    }

    @Test
    void setUser2Id() {
        friend.setUser2Id("c");
        assertEquals("c", friend.getUser2Id());
    }

    @Test
    void testToString() {
        assertEquals("Friend(friendId=1, user1Id=1, user2Id=2)", friend.toString());
    }
}