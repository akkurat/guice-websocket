package ch.taburett.jass.game.impl.internal;

import ch.taburett.jass.cards.JassCard;
import ch.taburett.jass.game.impl.PlayerReference;
import ch.taburett.jass.game.pub.log.ImmutableLogEntry;
import ch.taburett.jass.game.pub.log.ImmutbleTurn;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Jass Turn
 * -> Fixed value the number of Players (generally four)
 */
public class Turn {

    List<ImmutableLogEntry> log = new ArrayList<>();
    private int numberPlayers;

    public Turn(int numberPlayers) {
        this.numberPlayers = numberPlayers;
    }

    public void play(PlayerReference playerReference, JassCard card) {
        log.add(new ImmutableLogEntry(playerReference, card, LocalDateTime.now()));
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
