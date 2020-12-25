package com.asafalima.websocket.services;

import ch.taburett.jass.game.Game;
import ch.taburett.jass.game.spi.IRoundSupplier;
import ch.taburett.jass.game.spi.def.BoringRoundSupplier;
import ch.taburett.jass.game.spi.def.ModeDesider;

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
    public ProxyGame create(ProxyUser owner) {
        IRoundSupplier mode = new BoringRoundSupplier();
        return new ProxyGame( mode, owner );
    }
}
