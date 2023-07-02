package id.ac.ui.cs.gameservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MoveDTO {
    
    String gameId;

    int index;

    String userId;
}
