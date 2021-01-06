package ch.taburett.jass.game.pub.log;

import ch.taburett.jass.cards.JassCard;
import ch.taburett.jass.game.api.IPlayerReference;
import ch.taburett.jass.game.impl.PlayerReference;

import java.time.LocalDateTime;

public class ImmutableLogEntry {
    public final IPlayerReference playerReference;
    public final JassCard card;
    public final LocalDateTime dateTime;

    public ImmutableLogEntry(IPlayerReference playerReference, JassCard card, LocalDateTime dateTime) {
        this.playerReference = playerReference;
        this.card = card;
        this.dateTime = dateTime;
    }

    @Override
    public String toString() {
        return "LogEntry{" +
                "playerReference=" + playerReference.getRef() +
                ", card=" + card +
                ", now=" + dateTime +
                '}';
    }
}
