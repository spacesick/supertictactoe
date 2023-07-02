package id.ac.ui.cs.supertictactoe.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import id.ac.ui.cs.supertictactoe.model.GameMessage;
import id.ac.ui.cs.supertictactoe.model.RoomMessage;

@Controller
public class HandshakeController {

    @MessageMapping("/play/{gameId}")
    @SendTo("/topic/games/{gameId}")
    public final GameMessage sendMove(@DestinationVariable final String gameId, final GameMessage message) {
        return message;
    }

    @MessageMapping("/join/{roomId}")
    @SendTo("/topic/room/{roomId}")
    public final RoomMessage updatePlayer(@DestinationVariable final String roomId, final RoomMessage message) {
        return message;
    }
}
