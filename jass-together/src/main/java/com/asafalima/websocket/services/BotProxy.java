package com.asafalima.websocket.services;

import ch.taburett.jass.cards.JassCard;
import ch.taburett.jass.game.Game;
import ch.taburett.jass.game.spi.messages.IJassMessage;
import ch.taburett.jass.game.spi.messages.Play;
import ch.taburett.jass.game.spi.messages.YourTurn;

public class BotProxy implements IProxyUser{
    private String getUserName;
    private Game.PlayerReference getReference;

    BotProxy(String getUserName, Game.PlayerReference getReference) {
        this.getUserName = getUserName;
        this.getReference = getReference;
    }

    @Override
    public String getUserName() {
        return getUserName;
    }

    @Override
    public Game.PlayerReference getReference() {
        return getReference;
    }

    @Override
    public void receivePlayerMsg(IJassMessage msg) {
    }

    @Override
    public void receiveServerMessage(IJassMessage msg) {
        System.out.println(msg);
        if(msg instanceof YourTurn yt) {
            YourTurn.Payload pl = yt.getPayload();
            JassCard card = pl.getAvailCards().get(0);
            getReference.sendToServer( new Play(card) );
        }
    }

}
