package ch.taburett.gameworld.endpoints;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketStompChat {

    @MessageMapping("/messages")
    @SendTo("/chat/messages")
    public String convert( String wrapper )
    {
        return wrapper;
    }
}
