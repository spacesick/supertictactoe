package id.ac.ui.cs.supertictactoe.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestFriendDTO {

    String senderId;

    String receiverId;
}
