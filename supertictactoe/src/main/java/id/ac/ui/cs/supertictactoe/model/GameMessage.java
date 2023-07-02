package id.ac.ui.cs.supertictactoe.model;

import lombok.Data;

@Data
public class GameMessage {
    
    int markPos;

    Mark mark;

    String winner;

    // Jackson apparently removes 'is' when deserializing, so for
    // the sake of consistency, I've renamed this to just 'over'.
    boolean over;
}
