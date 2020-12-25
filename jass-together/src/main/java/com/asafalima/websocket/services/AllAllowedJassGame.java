package com.asafalima.websocket.services;

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
    public ProxyGame create(ProxyUser owner) {
        return null;
    }
}
