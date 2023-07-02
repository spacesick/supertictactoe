package id.ac.ui.cs.gameservice.controller;


import id.ac.ui.cs.gameservice.model.Game;
import id.ac.ui.cs.gameservice.model.MoveStatus;
import id.ac.ui.cs.gameservice.model.dto.GameCreationDTO;
import id.ac.ui.cs.gameservice.model.dto.MoveDTO;
import id.ac.ui.cs.gameservice.service.GameService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
public class GameController {

    @Autowired
    private GameService gameService;

    @MutationMapping
    public Game createGame(@Argument GameCreationDTO gameCreationDTO) {
        return gameService.initGame(gameCreationDTO.getPlayerUserIds(), gameCreationDTO.getPlayerUsernames(), gameCreationDTO.getBoardSize(), gameCreationDTO.getWinLength());
    }
    
    @QueryMapping
    public List<Game> gamesByPlayerUsername(@Argument String username) {
        return gameService.getGamesByPlayerUsername(username);
    }

    @QueryMapping
    public Game gameById(@Argument String id) {
        return gameService.getGameById(id);
    }

    @MutationMapping
    public MoveStatus playerMove(@Argument MoveDTO moveDTO) {
        return gameService.doMove(moveDTO.getGameId(), moveDTO.getIndex(), moveDTO.getUserId());
    }
}
