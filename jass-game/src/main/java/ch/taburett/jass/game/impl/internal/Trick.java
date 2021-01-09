package ch.taburett.jass.game.impl.internal;

import ch.taburett.jass.cards.JassCard;
import ch.taburett.jass.game.impl.PlayerReference;
import ch.taburett.jass.game.pub.log.ImmutableLogEntry;
import ch.taburett.jass.game.pub.log.GenericImmutableTrick;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Jass Turn
 * -> Fixed value the number of Players (generally four)
 */
public class Trick {

    List<ImmutableLogEntry> log = new ArrayList<>();
    private int numberPlayers;

    public Trick(int numberPlayers) {
        this.numberPlayers = numberPlayers;
    }

    public void play(PlayerReference playerReference, JassCard card) {
        log.add(new ImmutableLogEntry(playerReference, card, LocalDateTime.now()));
    }


    public boolean hasEnded() {
        return log.size() == numberPlayers;
    }

    GenericImmutableTrick getImmutableTrick() {
        return new GenericImmutableTrick(log);
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
