package com.asafalima.websocket.services;

import ch.taburett.jass.game.spi.events.IJassMessage;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

import static com.asafalima.websocket.services.ProxyGame.GAME_PLAY;

public class SubscriptionSink {
    private final SimpMessageSendingOperations simp;
    private final String sessionId;
    private final String gameId;
    private final String subscriptionId;
    private final ProxyUser playerByName;

    public SubscriptionSink(SimpMessageSendingOperations simp,
                            String gameId, String sessionId,
                            String subscriptionId, ProxyUser playerByName) {
        this.simp = simp;
        this.sessionId = sessionId;
        this.gameId = gameId;
        this.subscriptionId = subscriptionId;
        this.playerByName = playerByName;
    }

    public void sendToUser(String s, IJassMessage iJassMessage)
    {
        this.simp.convertAndSendToUser(s, GAME_PLAY + gameId, iJassMessage);
    }

    public ProxyUser getPlayerByName() {
        return playerByName;
    }

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public String getSessionId() {
        return sessionId;
    }
}
