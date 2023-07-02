package id.ac.ui.cs.supertictactoe.model.dto;

import java.util.List;

import id.ac.ui.cs.supertictactoe.model.Mark;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameDTO {

    String id;

    List<Mark> marks;

    int boardSize;

    boolean isOver;

    List<String> playerUsernames;

    String winnerUsername;

    int winnerNum;
}
