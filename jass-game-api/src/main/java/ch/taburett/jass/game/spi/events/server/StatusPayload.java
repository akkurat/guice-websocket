package ch.taburett.jass.game.spi.events.server;

import ch.taburett.jass.cards.JassCard;
import ch.taburett.jass.game.api.ITeam;
import ch.taburett.jass.game.pub.log.ImmutableLogEntry;
import ch.taburett.jass.game.pub.log.ImmutableRound;
import ch.taburett.jass.game.spi.IParmeterizedRound;
import ch.taburett.jass.game.spi.TrickMode;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StatusPayload {
    public final List<JassCard> availCards;
    public List<JassCard> legalCards;
    public final List<ImmutableLogEntry> roundCards;
    public final boolean yourTurn;
    public final Map<? extends ITeam, ? extends Integer> points;
    public final TrickMode mode;
    public final List<ImmutableRound> gameInfoPoints;

    public StatusPayload(List<JassCard> availCards,
                         List<JassCard> legalCards, List<ImmutableLogEntry> roundCards,
                         boolean yourTurn,
                         Map<? extends ITeam, ? extends Integer> points,
                         TrickMode mode,
                         List<ImmutableRound> gameInfoPoints) {
        this.availCards = availCards.stream()
                .sorted(JassCard.c())
                .collect(Collectors.toList());
        this.legalCards = List.copyOf(legalCards);
        this.roundCards = List.copyOf(roundCards);
        this.yourTurn = yourTurn;
        this.points = points;
        this.mode = mode;
        this.gameInfoPoints = gameInfoPoints;
    }
}
