package ch.taburett.gameworld.services;

import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Service
public class CmdSendingService {

    private SimpMessageSendingOperations messaging;

    public CmdSendingService(
            SimpMessageSendingOperations messaging) {
        this.messaging = messaging;
    }

    public void broadcastCmdReaction(String orig) {
        messaging.convertAndSend("/game/cmds", orig);
        messaging.convertAndSend("/game/gametypes", orig);
    }


}
