package id.ac.ui.cs.supertictactoe.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestApproval {

    String status;

    String message;
}
