package com.asafalima.websocket.services;

import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


@Service
public class GameLogic {

    private final ScheduledExecutorService executor;
    private CmdSendingService sender;

    public GameLogic(CmdSendingService sender) {
        this.sender = sender;
        executor = Executors.newSingleThreadScheduledExecutor();
    }

    public void react( String message ) {
       executor.schedule( () -> sendToChannel(message), 2, TimeUnit.SECONDS );
    }


    void sendToChannel( String message ) {
        sender.broadcastCmdReaction( "Reaction to " + message );
    }


}
