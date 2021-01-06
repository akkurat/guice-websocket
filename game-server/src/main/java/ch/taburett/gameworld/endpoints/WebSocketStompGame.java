package ch.taburett.gameworld.endpoints;

import ch.taburett.gameworld.services.GameCmdPlay;
import ch.taburett.gameworld.services.GameLogic;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
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

    @MessageMapping("/cmds/remove")
    public void deleteGame(@Payload GameCmdDelete wrapper, SimpMessageHeaderAccessor headerAccessor )
    {
        System.out.println(wrapper);
        gl.deleteGame(wrapper, headerAccessor);
    }

    @MessageMapping("/cmds/join")
    public void join( @Payload GameCmdJoin join, SimpMessageHeaderAccessor header) {
        gl.join( join, header );
    }

    @MessageMapping("/cmds/play")
    public void play(@Payload GameCmdPlay play, SimpMessageHeaderAccessor header) {
        gl.play( play, header );
    }
//    @MessageMapping("/cmds/play/{gameId}")
//    @Order(10)
//    public void play(@DestinationVariable("gameId") String gameId, @Payload Object obj, SimpMessageHeaderAccessor header) {
//        System.out.println(obj);
//    }
}
