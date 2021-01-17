package ch.taburett.gameworld.services;

import ch.taburett.jass.cards.JassCard;
import ch.taburett.jass.game.spi.events.server.IServerMessage;
import ch.taburett.jass.game.spi.events.server.ModeEvent;
import ch.taburett.jass.game.spi.events.server.Status;
import ch.taburett.jass.game.spi.events.server.StatusPayload;
import ch.taburett.jass.game.spi.events.user.DecideEvent;
import ch.taburett.jass.game.spi.events.user.IUserEvent;
import ch.taburett.jass.game.spi.events.user.Play;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;

public class BotProxy {


    private Consumer<IUserEvent<?>> sink;

    public BotProxy(Consumer<IUserEvent<?>> sink) {
        this.sink = sink;
    }

    public void receiveServerMessage(String name, IServerMessage<?> msg) {
        //System.out.println(msg);
        if(msg instanceof Status) {
            Status yt = (Status) msg;
            StatusPayload pl = yt.getPayload();
            if( pl.yourTurn ) {
//                try {
//                    Thread.sleep(1);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                JassCard card = pl.legalCards.get(0);
                sink.accept(new Play(card));
            }
        } else if ( msg instanceof ModeEvent) {
            ModeEvent e = (ModeEvent) msg;
            var keys = new ArrayList<>(e.getPayload().modes.keySet());
            var key =  keys.get(ThreadLocalRandom.current().nextInt(keys.size()));

            sink.accept(new DecideEvent(new DecideEvent.DecideParams(key,null)));

        }
    }


}
