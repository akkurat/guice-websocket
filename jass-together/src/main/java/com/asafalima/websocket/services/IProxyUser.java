package com.asafalima.websocket.services;

import ch.taburett.jass.game.Game;
import ch.taburett.jass.game.spi.messages.IJassMessage;

public interface IProxyUser {

    String getUserName();

    Game.PlayerReference getReference();

    void receivePlayerMsg(IJassMessage msg);

    void receiveServerMessage(IJassMessage msg);

    default void connect() {
        getReference().setProxy(this::receiveServerMessage);
    }
}
