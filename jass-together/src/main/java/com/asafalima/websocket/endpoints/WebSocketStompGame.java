package com.asafalima.websocket.endpoints;

import com.asafalima.websocket.messages.GameMessageWrapper;
import com.asafalima.websocket.services.GameLogic;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketStompGame {

    private GameLogic gl;

    public WebSocketStompGame(GameLogic gl) {
        this.gl = gl;
    }

    @MessageMapping("/cmds")
    public String convert( String wrapper )
    {
        System.out.println(wrapper);
        gl.react(wrapper);
        return wrapper;
    }
}
