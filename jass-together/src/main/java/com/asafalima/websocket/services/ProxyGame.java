package com.asafalima.websocket.services;

import ch.taburett.jass.game.spi.IRoundSupplier;

public class ProxyGame {
    private IRoundSupplier mode;
    private ProxyUser owner;

    public ProxyGame(IRoundSupplier mode, ProxyUser owner) {
        this.mode = mode;
        this.owner = owner;
    }
}
