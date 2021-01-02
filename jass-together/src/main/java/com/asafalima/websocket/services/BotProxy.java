package com.asafalima.websocket.services;

import ch.taburett.jass.cards.JassCard;
import ch.taburett.jass.game.spi.messages.IJassMessage;
import ch.taburett.jass.game.spi.messages.Play;
import ch.taburett.jass.game.spi.messages.Status;
import ch.taburett.jass.game.spi.messages.StatusPayload;

import java.util.function.Consumer;

public class BotProxy {


    private Consumer<Play> sink;

    public BotProxy(Consumer<Play> sink) {
        this.sink = sink;
    }

    public void receiveServerMessage(String name, IJassMessage msg) {
        System.out.println(msg);
        if(msg instanceof Status) {
            Status yt = (Status) msg;
            StatusPayload pl = yt.getPayload();
            if( pl.yourTurn ) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                JassCard card = pl.availCards.get(0);
                sink.accept(new Play(card));
            }
        }
    }


}
