package com.asafalima.websocket.services;

import ch.taburett.jass.cards.JassCard;
import ch.taburett.jass.game.spi.messages.*;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;

public class BotProxy {


    private Consumer<IJassMessage> sink;

    public BotProxy(Consumer<IJassMessage> sink) {
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
        } else if ( msg instanceof ModeEvent ) {
            ModeEvent e = (ModeEvent) msg;
            var keys = new ArrayList<>(e.getPayload().keySet());
            var key =  keys.get(ThreadLocalRandom.current().nextInt(keys.size()));

            sink.accept(new DecideEvent(new DecideEvent.DecideParams(key,null)));

        }
    }


}
