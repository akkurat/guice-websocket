package ch.taburett.jass.game.spi.messages;

import ch.taburett.jass.cards.JassCard;
import ch.taburett.jass.game.Turn.LogEntry;

import java.util.List;

public class StatusPayload {
    public final List<JassCard> availCards;
    public final List<LogEntry> roundCards;
    public final boolean yourTurn;

    public StatusPayload(List<JassCard> availCards, List<LogEntry> roundCards, boolean yourTurn) {
        this.availCards = List.copyOf(availCards);
        this.roundCards = List.copyOf(roundCards);
        this.yourTurn = yourTurn;
    }
}
