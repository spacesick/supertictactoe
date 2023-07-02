package id.ac.ui.cs.gameservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import id.ac.ui.cs.gameservice.model.*;
import id.ac.ui.cs.gameservice.repository.GameRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class GameServiceImpl implements GameService {

    @Autowired
    private GameRepository gameRepository;

    @Override
    public Game initGame(List<String> playerUserIds, List<String> playerUsernames, int boardSize, int winLength) {
        List<Mark> initMarkings = new ArrayList<>(Arrays.asList(new Mark[boardSize * boardSize]));
        Collections.fill(initMarkings, Mark.NONE);

        var game = Game.builder()
                .isOver(false)
                .currentPlayerTurnIndex(0)
                .size(boardSize)
                .marks(initMarkings)
                .winLength(winLength);

        for (int i = 0; i < playerUserIds.size(); ++i) {
            game.player(
                Player.builder()
                    .userId(playerUserIds.get(i))
                    .username(playerUsernames.get(i))
                    .mark(Mark.values()[i + 1])
                    .build()
            );
        }

        return gameRepository.save(game.build());
    }

    /**
     * Returns true if the move is valid, false otherwise.
     */
    @Override
    public MoveStatus doMove(String gameId, int index, String userId) {
        var game = gameRepository.getReferenceById(gameId);
        if (game.isOver()) {
            return new MoveStatus(false, Mark.NONE, null, true);
        }

        int turn = game.getCurrentPlayerTurnIndex();

        var player = game.getPlayers().get(turn);
        if (!userId.equals(player.getUserId())) {
            return new MoveStatus(false, Mark.NONE, null, false);
        }

        var marks = game.getMarks();
        if (marks.get(index) != Mark.NONE || marks.size() <= index || index < 0) {
            return new MoveStatus(false, Mark.NONE, null, false);
        }
        var playerMark = player.getMark();
        marks.set(index, playerMark);

        var boardSize = game.getSize();
        int xPos = index % boardSize;
        int yPos = index / boardSize;

        if (isWinningMove(game.getMarks(), game.getWinLength(), game.getSize(), xPos, yPos, playerMark)) {
            game.setOver(true);
            game.setWinnerIndex(turn);
            game.setCurrentPlayerTurnIndex(-1);

            saveAsync(game);

            return new MoveStatus(true, playerMark, player, true);
        }
        else if (isFull(game.getMarks())) {
            game.setOver(true);
            game.setCurrentPlayerTurnIndex(-1);

            saveAsync(game);

            return new MoveStatus(true, playerMark, null, true);
        }
        else {
            if (++turn == game.getPlayers().size())
                turn = 0;
            game.setCurrentPlayerTurnIndex(turn);

            saveAsync(game);

            return new MoveStatus(true, playerMark, null, false);
        }
    }
    
    private boolean isFull(List<Mark> marks) {
        for (Mark m : marks) {
            if (m == Mark.NONE)
                return false;
        }

        return true;
    }

    private boolean isNotOutOfBounds(int n, int boardSize) {
        return n >= 0 && n <= boardSize - 1;
    }

    private boolean isWinningMove(List<Mark> marks, int winLength, int size, int xPos, int yPos, Mark mark) {
        var l = winLength - 1;

        var mainDiagCount = 0;
        var antiDiagCount = 0;
        var rowCount = 0;
        var columnCount = 0;

        // Only need to search the squares surrounding the square position of the last move
        for (int i = -l; i <= l; i++) {
            // Checks row adjacent squares
            if (isNotOutOfBounds(xPos + i, size)) {
                if (marks.get((xPos + i) + yPos * size) != mark)
                    rowCount = 0;
                else
                    ++rowCount;
            }

            // Checks column adjacent squares
            if (isNotOutOfBounds(yPos + i, size)) {
                if (marks.get(xPos + (yPos + i) * size) != mark)
                    columnCount = 0;
                else
                    ++columnCount;
            }

            // Checks main diagonal adjacent squares
            if (isNotOutOfBounds(xPos + i, size) && isNotOutOfBounds(yPos + i, size)) {
                if (marks.get((xPos + i) + (yPos + i) * size) != mark)
                    mainDiagCount = 0;
                else
                    ++mainDiagCount;
            }

            // Checks anti diagonal adjacent squares
            if (isNotOutOfBounds(xPos + i, size) && isNotOutOfBounds(yPos - i, size)) {
                if (marks.get((xPos + i) + (yPos - i) * size) != mark)
                    antiDiagCount = 0;
                else
                    ++antiDiagCount;
            }

            if (rowCount == winLength || columnCount == winLength || mainDiagCount == winLength || antiDiagCount == winLength)
                return true;
        }

        return false;
    }

    @Override
    public Game getGameById(String id) {
        if (gameRepository.findById(id).equals(Optional.empty())) {
            return null;
        }

        return gameRepository.getReferenceById(id);
    }

    @Override
    public List<Game> getGamesByPlayerUsername(String username) {
        return gameRepository.findByPlayersUsername(username);
    }

    @Async
    private void saveAsync(Game game) {
        gameRepository.save(game);
    }    
}
