package id.ac.ui.cs.supertictactoe.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import id.ac.ui.cs.supertictactoe.model.GameMessage;
import id.ac.ui.cs.supertictactoe.model.Room;
import id.ac.ui.cs.supertictactoe.model.RoomMessage;
import id.ac.ui.cs.supertictactoe.model.User;
import id.ac.ui.cs.supertictactoe.service.RoomService;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class HandshakeControllerTest {

    @MockBean
    private WebSocketStompClient webSocketStompClient;

    @MockBean
    private RoomService roomService;


    private User mockedAppUser1;


    private User mockedAppUser2;


    private Room mockedRoom;

    @BeforeEach
    public void setup() {
        this.webSocketStompClient = new WebSocketStompClient(new SockJsClient(
                List.of(new WebSocketTransport(new StandardWebSocketClient()))));
    }

    @Test
    void verifySendMove() throws Exception {

        BlockingQueue<GameMessage> blockingQueue = new ArrayBlockingQueue(1);

        webSocketStompClient.setMessageConverter(new MappingJackson2MessageConverter());

        StompSession session = webSocketStompClient
                .connect("ws://localhost:8080/gs-guide-websocket", new StompSessionHandlerAdapter() {
                })
                .get(1, SECONDS);

        session.subscribe("/topic/games/g0", new StompFrameHandler() {

            @Override
            public Type getPayloadType(StompHeaders headers) {
                return GameMessage.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                blockingQueue.add((GameMessage) payload);
            }
        });

        GameMessage message = new GameMessage();
        message.setMarkPos(0);
        message.setOver(false);

        session.send("/app/play/g0", message);

        GameMessage result = blockingQueue.poll(1,SECONDS);
        assertEquals(0, result.getMarkPos());
        assertEquals(false, result.isOver());
    }

    @Test
    void verifyUpdatePlayer() throws Exception {

        BlockingQueue<RoomMessage> blockingQueue = new ArrayBlockingQueue(1);

        webSocketStompClient.setMessageConverter(new MappingJackson2MessageConverter());

        StompSession session = webSocketStompClient
                .connect("ws://localhost:8080/gs-guide-websocket", new StompSessionHandlerAdapter() {
                })
                .get(1, SECONDS);

        session.subscribe("/topic/room/r0", new StompFrameHandler() {

            @Override
            public Type getPayloadType(StompHeaders headers) {
                return RoomMessage.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                blockingQueue.add((RoomMessage) payload);
            }
        });

        RoomMessage message = new RoomMessage();

        message.setStarted(false);

        session.send("/app/join/r0", message);

        RoomMessage result = blockingQueue.poll(1,SECONDS);

        assertEquals(false, result.isStarted());
    }

}
