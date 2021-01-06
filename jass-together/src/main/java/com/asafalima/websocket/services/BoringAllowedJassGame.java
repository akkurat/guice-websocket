package com.asafalima.websocket.services;

import ch.taburett.jass.game.impl.internal.Game;
import ch.taburett.jass.game.spi.IRoundSupplier;
import ch.taburett.jass.game.spi.impl.BoringRoundSupplier;
import org.springframework.messaging.simp.SimpMessagingTemplate;

public class BoringAllowedJassGame implements ProxyInstanceableGame{
    @Override
    public int minPlayers() {
        return 4;
    }

    @Override
    public String getCaption() {
        return "Boring Schieber einfach";
    }

    @Override
    public ProxyGame create(String owner, SimpMessagingTemplate simp) {
        IRoundSupplier mode = new BoringRoundSupplier();
        return new ProxyGame(owner, this, new Game(mode), simp);
    }
}
