package ch.taburett.jass.game.spi.events.server;

import ch.taburett.jass.cards.JassCard;
import ch.taburett.jass.game.api.ITeam;
import ch.taburett.jass.game.impl.internal.Team;
import ch.taburett.jass.game.pub.log.ImmutableLogEntry;
import ch.taburett.jass.game.spi.IParmeterizedRound;

import java.util.List;
import java.util.Map;

public class StatusPayload {
    public final List<JassCard> availCards;
    public final List<ImmutableLogEntry> roundCards;
    public final boolean yourTurn;
    public final Map<? extends ITeam, ? extends Integer> points;
    public final IParmeterizedRound mode;
    public final List<? extends Map<? extends ITeam, ? extends Integer>> gameInfoPoints;

    public StatusPayload(List<JassCard> availCards,
                         List<ImmutableLogEntry> roundCards,
                         boolean yourTurn,
                         Map<? extends ITeam, ? extends Integer> points,
                         IParmeterizedRound mode,
                         List<?extends Map<?extends ITeam, ? extends Integer>>gameInfoPoints) {
        this.availCards = List.copyOf(availCards);
        this.roundCards = List.copyOf(roundCards);
        this.yourTurn = yourTurn;
        this.points = points;
        this.mode = mode;
        this.gameInfoPoints = gameInfoPoints;
    }
}
