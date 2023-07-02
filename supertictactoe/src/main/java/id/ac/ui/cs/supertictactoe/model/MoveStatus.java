package id.ac.ui.cs.supertictactoe.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MoveStatus {

    boolean isValid;

    Mark mark;

    Player winner;

    boolean isOver;
}
