package com.asafalima.websocket.services;

import ch.taburett.jass.game.impl.internal.Game;
import ch.taburett.jass.game.spi.impl.ModeDesider;
import org.springframework.messaging.simp.SimpMessagingTemplate;

public class AllAllowedJassGame implements ProxyInstanceableGame {
    @Override
    public int minPlayers() {
        return 4;
    }

    @Override
    public String getCaption() {
        return "Alles Erlaubt Schieber, Mehrfach Zählung";
    }

    @Override
    public ProxyGame create(String owner, SimpMessagingTemplate simp) {
        Game gamge = new Game(new ModeDesider());
        return new ProxyGame(owner, this, gamge, simp);
    }

}
