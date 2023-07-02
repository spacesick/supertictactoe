package id.ac.ui.cs.friendservice.model.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import id.ac.ui.cs.friendservice.model.dto.UserIdListDTO;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserIdListDTOTest {

    private UserIdListDTO userIdListDTO;

    @BeforeEach
    public void setUp() {
        userIdListDTO = new UserIdListDTO(List.of(new String[]{"a", "b", "c"}));
    }


    @Test
    void getUserIds() {
        assertArrayEquals(new String[]{"a", "b", "c"}, userIdListDTO.getUserIds().toArray());
    }

    @Test
    void setUserIds() {
        userIdListDTO.setUserIds(List.of(new String[]{"a"}));
        assertArrayEquals(new String[]{"a"}, userIdListDTO.getUserIds().toArray());
    }

    @Test
    void testEquals() {
        assertTrue(userIdListDTO.equals(userIdListDTO));
    }

    @Test
    void canEqual() {
        assertTrue(userIdListDTO.canEqual(new UserIdListDTO(List.of(new String[]{"a"}))));
    }

    @Test
    void testHashCode() {
        UserIdListDTO other = new UserIdListDTO(List.of(new String[]{"a", "b", "c"}));
        assertEquals(other.hashCode(), userIdListDTO.hashCode());
    }

    @Test
    void testToString() {
        assertEquals("UserIdListDTO(userIds=[a, b, c])",userIdListDTO.toString());
    }
}