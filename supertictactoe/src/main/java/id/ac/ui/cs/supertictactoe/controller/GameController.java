package id.ac.ui.cs.supertictactoe.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import id.ac.ui.cs.supertictactoe.exception.ForbiddenGameSettingsException;
import id.ac.ui.cs.supertictactoe.exception.GameNotFoundException;
import id.ac.ui.cs.supertictactoe.exception.RoomNotFoundException;
import id.ac.ui.cs.supertictactoe.exception.UnwinnableGameException;
import id.ac.ui.cs.supertictactoe.model.Game;
import id.ac.ui.cs.supertictactoe.model.Mark;
import id.ac.ui.cs.supertictactoe.model.MoveStatus;
import id.ac.ui.cs.supertictactoe.model.RoomMessage;
import id.ac.ui.cs.supertictactoe.model.User;
import id.ac.ui.cs.supertictactoe.service.GameService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;

@Controller
@RequestMapping("/")
public class GameController {

    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private GameService gameService;

    private String messageAttr = "message";

    private String errorPage = "error_page";

    private String invalidNewGameMessage = "Unable to create a new game, are you sure that the settings you specified were valid?";

    @GetMapping(value = "/{roomId}/play")
    public final String playNewGame(@PathVariable final String roomId, @RequestParam("b") final String boardSizeStr, @RequestParam("w") final String winningLengthStr, final Model model) {
        int boardSize;
        int winLength;
        try {
            boardSize = Integer.parseInt(boardSizeStr);
            winLength = Integer.parseInt(winningLengthStr);
        } catch (NumberFormatException e) {
            model.addAttribute(messageAttr, invalidNewGameMessage);
            return errorPage;
        }

        String gameId;
        try {
            gameId = gameService.initGame(roomId, boardSize, winLength);
        } catch (RoomNotFoundException e) {
            model.addAttribute(messageAttr, "The room you\'re trying to find does not exist, unfortunately. Sorry!");
            return errorPage;
        } catch (ForbiddenGameSettingsException e) {
            model.addAttribute(messageAttr, invalidNewGameMessage);
            return errorPage;
        } catch (UnwinnableGameException e) {
            model.addAttribute(messageAttr, "The win condition length you specified is unattainable. Please make sure that it is less than the board size.");
            return errorPage;
        }

        template.convertAndSend("/topic/room/" + roomId, new RoomMessage("", true, "games/" + gameId));
        
        return "redirect:/games/" + gameId;
    }

    @GetMapping(value = "/games/{gameId}")
    public final String gamePage(@PathVariable final String gameId, final Model model) {
        Game game;
        try {
            game = gameService.getGameById(gameId);
        } catch (GameNotFoundException e) {
            model.addAttribute("message", "The game you\'re trying to find does not exist, unfortunately. Sorry!");
            return errorPage;
        }
        model.addAttribute("game", game);
        
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var user = (User) auth.getPrincipal();
        model.addAttribute("userId", user.getUserId());

        return "game_page";
    }

    @PostMapping(value = "/service/{gameId}/move", produces = {"application/json"})
    public final ResponseEntity<MoveStatus> playerMove(@PathVariable final String gameId, @RequestParam("pos") final String posStr, final Model model) {
        int pos;
        try {
            pos = Integer.parseInt(posStr);
        } catch (NumberFormatException e) {
            return ResponseEntity.ok(new MoveStatus(false, Mark.NONE, null, false));
        }
        
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var user = (User) auth.getPrincipal();

        var moveStatus = gameService.doMove(gameId, pos, user.getUserId());

        return ResponseEntity.ok(moveStatus);
    }
}
