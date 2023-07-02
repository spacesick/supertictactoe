package id.ac.ui.cs.supertictactoe.service;

import java.util.List;

import id.ac.ui.cs.supertictactoe.model.Game;
import id.ac.ui.cs.supertictactoe.model.MoveStatus;
import id.ac.ui.cs.supertictactoe.model.Room;

public interface GameService {

    /**
     * Initializes a new game in the specified room
     * @param roomId
     * @return
     */
    String initGame(String roomId, int boardSize, int winLength);

    MoveStatus doMove(String gameId, int index, String userId);

    Game getGameById(String id);

    List<Game> getGamesByPlayerUsername(String username);

    Room getRoomById(String id);
}
