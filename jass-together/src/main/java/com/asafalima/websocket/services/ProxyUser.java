package com.asafalima.websocket.services;

import ch.taburett.jass.game.Game;
import ch.taburett.jass.game.spi.messages.IJassMessage;

public record ProxyUser(String getUserName, Game.PlayerReference getReference) implements IProxyUser {


    @Override
    public void receivePlayerMsg(IJassMessage msg) {

    }

    @Override
    public void receiveServerMessage(IJassMessage msg) {

    }


}
