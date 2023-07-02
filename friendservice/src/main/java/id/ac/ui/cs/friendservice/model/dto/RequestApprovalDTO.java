package id.ac.ui.cs.friendservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RequestApprovalDTO {
    String status;
    String message;
}
