package ch.taburett.jass.game.pub.log;

import ch.taburett.jass.cards.JassCard;
import ch.taburett.jass.game.api.IPlayerReference;
import ch.taburett.jass.game.api.ITeam;
import ch.taburett.jass.game.impl.PlayerReference;
import ch.taburett.jass.game.spi.IParmeterizedRound;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ImmutableRound {
    public final Map<PlayerReference, List<JassCard>> initalCards;
    private final List<GenericImmutableTrick> turns;
    public IParmeterizedRound parmeterizedRound;

    public ImmutableRound(
            Map<PlayerReference, List<JassCard>> initalCards,
            List<GenericImmutableTrick> turns,
            IParmeterizedRound parmeterizedRound
    ) {
        this.initalCards = Map.copyOf(initalCards);
        this.turns = List.copyOf(turns);
        this.parmeterizedRound = parmeterizedRound;
    }

    public Map<IPlayerReference, Integer> getPointsByPlayer() {
        var mode = parmeterizedRound.getCountMode();

        var between = turns.stream()
                .collect(
                        Collectors.groupingBy(t -> t.whoTakes(parmeterizedRound.getRankMode()),
                                Collectors.summingInt(t -> t.sum(mode))
                        ));
        return between;
    }

    public Map<ITeam, Integer> getPointsByTeam() {
        var countMode = parmeterizedRound.getCountMode();
        var rankMode = parmeterizedRound.getRankMode();

        var between = turns.stream()
                .collect(Collectors.groupingBy(t -> t.whoTakes(rankMode).getTeam(),
                        Collectors.summingInt(t -> t.sum(countMode))
                ));
        return between;
    }

    public List<ImmutableTrick> getParametrizedTurns() {
        return turns.stream()
                .map(this::parametrized)
                .collect(Collectors.toUnmodifiableList());
    }

    private ImmutableTrick parametrized(GenericImmutableTrick trick) {
        return new ImmutableTrick(
                trick.log,
                trick.whoTakes(parmeterizedRound.getRankMode()),
                trick.sum(parmeterizedRound.getCountMode())
        );
    }

    public Map<ITeam, Integer> getLastRoundBonus() {
        if (turns.size() == 9) {
            var rankMode = parmeterizedRound.getRankMode();
            GenericImmutableTrick lastTurn = turns.get(turns.size() - 1);
            var team = lastTurn.whoTakes(rankMode).getTeam();
            return Map.of(team, 5);
        } else {
            return Map.of();
        }

    }

    public Map<ITeam, Integer> getTotalPointsByTeam() {
        Map<ITeam, Integer> map = getPointsByTeam();
        getLastRoundBonus().forEach((k, v) -> map.merge(k, v, Integer::sum));
        parmeterizedRound.getCountMode().transformRoundResult(map);
        return map;
    }
}
