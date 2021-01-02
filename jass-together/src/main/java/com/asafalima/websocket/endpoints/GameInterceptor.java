package com.asafalima.websocket.endpoints;

import com.asafalima.websocket.services.GameStorage;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.security.Principal;

@Component
public class GameInterceptor implements ChannelInterceptor {


    private GameStorage gs;

    public GameInterceptor(GameStorage gs) {
        this.gs = gs;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor headerAccessor= StompHeaderAccessor.wrap(message);
//        if (StompCommand.SUBSCRIBE.equals(headerAccessor.getCommand())) {
//            Principal userPrincipal = headerAccessor.getUser();
//            if(!gs.isGameEndpointAllowed(headerAccessor) ){
//                throw new IllegalArgumentException("No permission for this game");
//            }
//        }
        return message;
    }
}