package id.ac.ui.cs.gameservice.service;

import java.util.List;

import id.ac.ui.cs.gameservice.model.Game;
import id.ac.ui.cs.gameservice.model.MoveStatus;

public interface GameService {

    Game initGame(List<String> playerUserIds, List<String> playerUsernames, int boardSize, int winLength);

    MoveStatus doMove(String gameId, int index, String userId);

    Game getGameById(String id);

    List<Game> getGamesByPlayerUsername(String username);
}
