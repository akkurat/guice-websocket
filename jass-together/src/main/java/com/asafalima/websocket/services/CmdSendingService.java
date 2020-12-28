package com.asafalima.websocket.services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Service
public class CmdSendingService {

    private SimpMessageSendingOperations messaging;

    @Autowired
    public CmdSendingService(
            SimpMessageSendingOperations messaging) {
        this.messaging = messaging;
    }

    public void broadcastCmdReaction(String orig) {
        messaging.convertAndSend("/game/cmds", orig);
        messaging.convertAndSend("/game/gametypes", orig);
    }


}
