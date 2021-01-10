package ch.taburett.jass.game.pub.log;

import ch.taburett.jass.cards.JassCard;
import ch.taburett.jass.game.api.IPlayerReference;
import ch.taburett.jass.game.impl.PlayerReference;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ImmutableLogEntry {
    public final IPlayerReference playerReference;
    public final JassCard card;
    public final LocalDateTime dateTime;


    @Override
    public String toString() {
        return "LogEntry{" +
                "playerReference=" + playerReference.getRef() +
                ", card=" + card +
                ", now=" + dateTime +
                '}';
    }

}
