package ch.taburett.jass.game;

import ch.taburett.jass.cards.JassCard;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Jass Turn
 * -> Fixed value the number of Players (generally four)
 */
public class Turn {

    List<LogEntry> log = new ArrayList<>();
    private int numberPlayers;

    public Turn(int numberPlayers) {
        this.numberPlayers = numberPlayers;
    }

    public void play(PlayerReference playerReference, JassCard card) {
        log.add(new LogEntry(playerReference, card, LocalDateTime.now()));
    }

    public Turn nextTurn(List<ImmutbleTurn> roundLog) {
        // Jass turn
        // tichu would be lastplayed card + 3x passed
        if (log.size() == numberPlayers) {
            roundLog.add(getImmutableTurn());
            return new Turn(numberPlayers);
        } else {
            return this;
        }
    }

    ImmutbleTurn getImmutableTurn() {
        return new ImmutbleTurn(log);
    }

    public static class LogEntry {
        public final PlayerReference playerReference;
        public final JassCard card;
        public final LocalDateTime dateTime;

        public LogEntry(PlayerReference playerReference, JassCard card, LocalDateTime dateTime) {
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

    boolean isFresh() {
        return log.isEmpty();
    }

    @Override
    public String toString() {
        return "Turn{" +
                "log=" + log +
                ", numberPlayers=" + numberPlayers +
                '}';
    }
}
