package ch.taburett.jass.game;

import ch.taburett.jass.cards.JassCard;
import ch.taburett.jass.game.spi.messages.IJassMessage;

import java.util.List;

public class DistributeEvent implements IJassMessage<List<JassCard>> {
    private final IPlayerReference from;
    private final IPlayerReference to;

    @Override
    public IPlayerReference getFrom() {
        return from;
    }

    @Override
    public IPlayerReference getTo() {
        return to;
    }

    @Override
    public List<JassCard> getPayload() {
        return payload;
    }

    private final List<JassCard> payload;

    DistributeEvent(IPlayerReference from, IPlayerReference to,
                    List<JassCard> payload) {
        this.from = from;
        this.to = to;
        this.payload = List.copyOf(payload);
    }

    @Override
    public String getCode() {
        return "DIST";
    }

}
