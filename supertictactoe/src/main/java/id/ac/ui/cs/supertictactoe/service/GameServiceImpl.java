package id.ac.ui.cs.supertictactoe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import id.ac.ui.cs.supertictactoe.exception.ForbiddenGameSettingsException;
import id.ac.ui.cs.supertictactoe.exception.GameNotFoundException;
import id.ac.ui.cs.supertictactoe.exception.RoomNotFoundException;
import id.ac.ui.cs.supertictactoe.exception.UnwinnableGameException;
import id.ac.ui.cs.supertictactoe.model.*;
import id.ac.ui.cs.supertictactoe.model.dto.GameCreationDTO;
import id.ac.ui.cs.supertictactoe.model.dto.MoveDTO;
import id.ac.ui.cs.supertictactoe.repository.RoomRepository;
import jakarta.annotation.PostConstruct;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GameServiceImpl implements GameService {

    @Autowired
    private RoomRepository roomRepository;

    @Value("${game.service.graphql.url}")
    private String SERVICE_URL;

    private WebClient client;

    private HttpGraphQlClient gqlClient;

    @PostConstruct
    public void init() {
        client = WebClient.create(SERVICE_URL);
        gqlClient = HttpGraphQlClient.create(client);
    }

    private String requestCreateGame(final User[] players, final int boardSize, final int winLength) {
        List<String> userIds = new ArrayList<>(4);
        List<String> usernames = new ArrayList<>(4);

        for (User player : players) {
            userIds.add(player.getUserId());
            usernames.add(player.getUsername());
        }

        var gameId = gqlClient.document("""
                    mutation CreateGame($gameCreationDTO: GameCreationDTO!) {
                        createGame(gameCreationDTO: $gameCreationDTO) {
                            id
                        }
                    }
                """)
                .variable("gameCreationDTO", new GameCreationDTO(userIds, usernames, boardSize, winLength))
                .retrieve("createGame.id")
                .toEntity(String.class);

        return gameId.block();
    }

    private MoveStatus requestPlayerMove(final MoveDTO moveDTO) {
        return gqlClient.document("""
                    mutation CommitPlayerMove($moveDTO: MoveDTO!) {
                        playerMove(moveDTO: $moveDTO) {
                            isValid
                            mark
                            winner {
                                username
                            }
                            isOver
                        }
                    }
                """)
                .variable("moveDTO", moveDTO)
                .execute()
                .map(response -> {
                    boolean isValid = response.field("playerMove.isValid").toEntity(Boolean.class);
                    var mark = response.field("playerMove.mark").toEntity(Mark.class);
                    var winner = response.field("playerMove.winner").toEntity(Player.class);
                    boolean isOver = response.field("playerMove.isOver").toEntity(Boolean.class);
                    return new MoveStatus(isValid, mark, winner, isOver);
                })
                .block();
    }

    private Game requestGameById(final String id) {
        var game = gqlClient.document("""
                    query QueryGameById($id: ID!) {
                        gameById(id: $id) {
                            id
                            marks
                            size
                            winLength
                            players {
                                username
                            }
                            winnerIndex
                            isOver
                        }
                    }
                """)
                .variable("id", id)
                .retrieve("gameById")
                .toEntity(Game.class);

        return game.block();
    }

    private List<Game> requestGamesByPlayerUsername(final String username) {
        return gqlClient.document("""
                    query QueryGamesByPlayerUsername($username: String!) {
                        gamesByPlayerUsername(username: $username) {
                            id
                        }
                    }
                """)
                .variable("username", username)
                .retrieve("gamesByPlayerUsername")
                .toEntityList(Game.class)
                .block();
    }

    @Override
    public final String initGame(final String roomId, final int boardSize, final int winLength)
            throws RoomNotFoundException, ForbiddenGameSettingsException, UnwinnableGameException {
        if (boardSize <= 0 || winLength <= 0) {
            throw new ForbiddenGameSettingsException();
        } else if (boardSize < winLength) {
            throw new UnwinnableGameException();
        }

        var room = getRoomById(roomId);

        User[] players = { room.getPlayer1(), room.getPlayer2() };

        return requestCreateGame(players, boardSize, winLength);
    }

    @Override
    public final MoveStatus doMove(final String gameId, final int index, final String userId) {
        return requestPlayerMove(new MoveDTO(gameId, index, userId));
    }

    @Override
    public final Game getGameById(final String id) throws GameNotFoundException {
        var game = requestGameById(id);
        if (game == null) {
            throw new GameNotFoundException();
        }
        return game;
    }

    @Override
    public final Room getRoomById(final String id) throws RoomNotFoundException {
        if (roomRepository.findById(id).equals(Optional.empty())) {
            throw new RoomNotFoundException();
        }

        return roomRepository.getReferenceById(id);
    }

    @Override
    public final List<Game> getGamesByPlayerUsername(final String username) {
        List<Game> currentGames = requestGamesByPlayerUsername(username);
        if (currentGames != null) {
            currentGames.removeIf(game -> !game.isOver());
        }
        return currentGames;
    }

}
