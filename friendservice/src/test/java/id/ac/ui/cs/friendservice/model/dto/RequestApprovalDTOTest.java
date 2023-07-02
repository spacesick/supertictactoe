package id.ac.ui.cs.friendservice.model.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import id.ac.ui.cs.friendservice.model.dto.RequestApprovalDTO;

import static org.junit.jupiter.api.Assertions.*;

class RequestApprovalDTOTest {

    private RequestApprovalDTO requestApprovalDTO;

    @BeforeEach
    public void setUp(){
      requestApprovalDTO = new RequestApprovalDTO("Status", "Message");
    }

    @Test
    void getStatus() {
        assertEquals("Status", requestApprovalDTO.getStatus());
    }

    @Test
    void getMessage() {
        assertEquals("Message", requestApprovalDTO.getMessage());
    }

    @Test
    void setStatus() {
        requestApprovalDTO.setStatus("New");
        assertEquals("New", requestApprovalDTO.getStatus());
    }

    @Test
    void setMessage() {
        requestApprovalDTO.setMessage("New");
        assertEquals("New", requestApprovalDTO.getMessage());
    }

    @Test
    void testEqualsCorrect() {
        assertTrue(requestApprovalDTO.equals(requestApprovalDTO));
    }

    @Test
    void testEqualsFalse() {
        assertFalse(requestApprovalDTO.equals(new RequestApprovalDTO("a","b")));
    }
    
    @Test
    void testCanEquals() {
        assertTrue(requestApprovalDTO.canEqual(new RequestApprovalDTO("a","b")));
    }

    @Test
    void testHashCode() {
        RequestApprovalDTO a = new RequestApprovalDTO("a","b");
        RequestApprovalDTO b = new RequestApprovalDTO("a","b");
        assertEquals(a.hashCode(), b.hashCode());

    }

    @Test
    void testToString() {
        assertEquals("RequestApprovalDTO(status=Status, message=Message)", requestApprovalDTO.toString());
    }
}