package id.ac.ui.cs.supertictactoe.model;


import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Game {

    private String id;

    List<Mark> marks;

    int size;

    int winLength;

    @Singular
    List<Player> players;

    int currentPlayerTurnIndex;

    @Builder.Default
    int winnerIndex = -1;

    boolean isOver;
}
