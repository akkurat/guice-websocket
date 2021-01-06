package ch.taburett.jass.game.spi.events.server;

import ch.taburett.jass.cards.JassCard;
import ch.taburett.jass.game.spi.events.IJassMessage;

import java.util.List;

public class DistributeEvent implements IServerMessage<List<JassCard>> {


    @Override
    public List<JassCard> getPayload() {
        return payload;
    }

    private final List<JassCard> payload;

    public DistributeEvent(List<JassCard> payload) {
        this.payload = List.copyOf(payload);
    }

    @Override
    public String getCode() {
        return "DIST";
    }

}
