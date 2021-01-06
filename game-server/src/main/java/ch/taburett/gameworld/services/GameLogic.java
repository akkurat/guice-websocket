package ch.taburett.gameworld.services;

import ch.taburett.gameworld.endpoints.GameCmdDelete;
import ch.taburett.gameworld.endpoints.GameCmdJoin;
import ch.taburett.gameworld.endpoints.GameCmdNew;
import ch.taburett.gameworld.endpoints.GameCmd;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


@Service
public class GameLogic {

    private final ScheduledExecutorService executor;
    private CmdSendingService sender;
    private GameList gl;

    public GameLogic(CmdSendingService sender, GameList gl) {
        this.sender = sender;
        this.gl = gl;
        executor = Executors.newSingleThreadScheduledExecutor();
    }

    public void react(GameCmd message, SimpMessageHeaderAccessor headerAccessor) {
        switch (message.getCmd())
        {
            case NEW_GAME: {
                String type = message.getPayload().get("type");
            }
        }
        executor.schedule( () -> sendToChannel(message.toString()), 2, TimeUnit.SECONDS );
    }

    void sendToChannel( String message ) {
        sender.broadcastCmdReaction( "Reaction to " + message );
    }

    public void newGame(GameCmdNew wrapper, SimpMessageHeaderAccessor headerAccessor) {
        var user = headerAccessor.getUser();
        gl.createGame(headerAccessor.getUser().getName(), wrapper.type);
    }
    public void deleteGame(GameCmdDelete wrapper, SimpMessageHeaderAccessor headerAccessor) {
        var user = headerAccessor.getUser();
        // TODO: probably a memory leak like that
        // -> use weak refrences in PlayerReference and so own...
        gl.deleteGame(wrapper.gameId);
    }

    public void join(GameCmdJoin join, SimpMessageHeaderAccessor header) {
        gl.join( join.id, header.getUser() );
    }

    public void play( GameCmdPlay play, SimpMessageHeaderAccessor header ) {

    }

}
