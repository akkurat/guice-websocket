package com.asafalima.websocket.endpoints;

import com.asafalima.websocket.messages.GameMessageWrapper;
import com.asafalima.websocket.services.GameCmdPlay;
import com.asafalima.websocket.services.GameLogic;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketStompGame {

    private GameLogic gl;

    public WebSocketStompGame(GameLogic gl) {
        this.gl = gl;
    }

    @MessageMapping("/cmds")
    public void convert(@Payload GameCmd wrapper, SimpMessageHeaderAccessor headerAccessor )
    {
        System.out.println(wrapper);
        gl.react(wrapper, headerAccessor);
    }

    @MessageMapping("/cmds/new")
    public void newGame(@Payload GameCmdNew wrapper, SimpMessageHeaderAccessor headerAccessor )
    {
        System.out.println(wrapper);
        gl.newGame(wrapper, headerAccessor);
    }

    @MessageMapping("/cmds/join")
    public void join( @Payload GameCmdJoin join, SimpMessageHeaderAccessor header) {
        gl.join( join, header );
    }

    @MessageMapping("/cmds/play")
    public void play(@Payload GameCmdPlay play, SimpMessageHeaderAccessor header) {
        gl.play( play, header );
    }
}
