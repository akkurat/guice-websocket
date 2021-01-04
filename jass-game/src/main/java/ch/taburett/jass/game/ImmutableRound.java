package ch.taburett.jass.game;

import ch.taburett.jass.cards.JassCard;
import ch.taburett.jass.game.spi.IParmeterizedRound;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ImmutableRound {
    private final Map<PlayerReference, List<JassCard>> initalCards;
    private final List<ImmutbleTurn> turns;
    private IParmeterizedRound parmeterizedRound;

    public ImmutableRound(
            Map<PlayerReference, List<JassCard>> initalCards,
            List<ImmutbleTurn> turns,
            IParmeterizedRound parmeterizedRound
    ) {
        this.initalCards = Map.copyOf(initalCards);
        this.turns = List.copyOf(turns);
        this.parmeterizedRound = parmeterizedRound;
    }

    public Map<PlayerReference, Integer> getPointsByPlayer() {
        var mode = parmeterizedRound.getCountMode();

        var between = turns.stream()
                .collect(
                        Collectors.groupingBy(t -> t.whoTakes(parmeterizedRound.getRankMode()),
                                Collectors.summingInt(t -> t.sum(mode))
                        ));
        return between;
    }

    public Map<Team, Integer> getPointsByTeam() {
        var countMode = parmeterizedRound.getCountMode();
        var rankMode = parmeterizedRound.getRankMode();

        var between = turns.stream()
                .collect(Collectors.groupingBy(t -> t.whoTakes(rankMode).getTeam(),
                        Collectors.summingInt(t -> t.sum(countMode))
                ));
        return between;
    }

    public Map<Team, Integer> getLastRoundBonus() {
        if(turns.size() == 9 ) {
            var rankMode = parmeterizedRound.getRankMode();
            ImmutbleTurn lastTurn = turns.get(turns.size() - 1);
            var team = lastTurn.whoTakes(rankMode).getTeam();
            return Map.of(team, 5);
        } else  {
            return Map.of();
        }

    }

    Map<Team, Integer> getTotalPointsByTeam() {
        Map<Team, Integer> map = getPointsByTeam();
        getLastRoundBonus().forEach((k,v) -> map.merge(k,v,Integer::sum));
        parmeterizedRound.getCountMode().transformRoundResult(map);
        return map;
    }
}
