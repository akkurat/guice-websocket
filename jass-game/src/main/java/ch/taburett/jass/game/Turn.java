package ch.taburett.jass.game;

import ch.taburett.jass.cards.JassCard;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Turn {

    List<LogEntry> log = new ArrayList<>();

    public void play(Game.PlayerReference playerReference, JassCard card) {
        log.add(new LogEntry(playerReference, card, LocalDateTime.now()));
        // Jass turn
        // tichu would be lastplayed card + 3x passed
    }

    public Turn nextTurn() {
        if(log.size() == 4) {
            return new Turn();
        }else {
            return this;
        }
    }

    public static class LogEntry {
        public final Game.PlayerReference playerReference;
        public final JassCard card;
        public final LocalDateTime now;

        public LogEntry(Game.PlayerReference playerReference, JassCard card, LocalDateTime now) {
            this.playerReference = playerReference;
            this.card = card;
            this.now = now;
        }
    }
}
