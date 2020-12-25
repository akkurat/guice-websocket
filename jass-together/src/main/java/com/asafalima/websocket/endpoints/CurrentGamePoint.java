package com.asafalima.websocket.endpoints;

import org.springframework.stereotype.Controller;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

@Controller
public class CurrentGamePoint extends AbstractWebSocketHandler {

    public CurrentGamePoint() {
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        System.out.println("New Text Message Received");
        session.sendMessage(message);
    }
}
