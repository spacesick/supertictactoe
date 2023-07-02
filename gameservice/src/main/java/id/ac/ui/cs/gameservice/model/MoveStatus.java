package id.ac.ui.cs.gameservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MoveStatus {
    
    boolean isValid;

    Mark mark;

    Player winner;

    boolean isOver;
}
