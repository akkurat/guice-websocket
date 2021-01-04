package ch.taburett.jass.game.spi.messages;

import ch.taburett.jass.cards.JassCard;
import ch.taburett.jass.game.Team;
import ch.taburett.jass.game.Turn.LogEntry;
import ch.taburett.jass.game.spi.IParmeterizedRound;

import java.util.List;
import java.util.Map;

public class StatusPayload {
    public final List<JassCard> availCards;
    public final List<LogEntry> roundCards;
    public final boolean yourTurn;
    public final Map<Team, Integer> points;
    public final IParmeterizedRound mode;
    public final List<Map<Team, Integer>> gameInfoPoints;

    public StatusPayload(List<JassCard> availCards, List<LogEntry> roundCards, boolean yourTurn, Map<Team, Integer> points, IParmeterizedRound mode, List<Map<Team, Integer>> gameInfoPoints) {
        this.availCards = List.copyOf(availCards);
        this.roundCards = List.copyOf(roundCards);
        this.yourTurn = yourTurn;
        this.points = points;
        this.mode = mode;
        this.gameInfoPoints = gameInfoPoints;
    }
}
