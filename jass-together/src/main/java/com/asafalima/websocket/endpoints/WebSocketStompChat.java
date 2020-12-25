package com.asafalima.websocket.endpoints;

import com.asafalima.websocket.messages.GameMessageWrapper;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.Mapping;

@Controller
public class WebSocketStompChat {

    @MessageMapping("/messages")
    @SendTo("/chat/messages")
    public String convert( String wrapper )
    {
        return wrapper;
    }
}
