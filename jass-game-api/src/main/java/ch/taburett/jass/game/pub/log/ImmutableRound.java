package ch.taburett.jass.game.pub.log;

import ch.taburett.jass.game.api.IPlayerReference;
import ch.taburett.jass.game.api.ITeam;
import ch.taburett.jass.game.spi.ICountModeParametrized;
import ch.taburett.jass.game.spi.IParmeterizedRound;
import ch.taburett.jass.game.spi.IRankModeParametrized;
import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ImmutableRound {
    private final List<GenericImmutableTrick> turns;
    public IParmeterizedRound parmeterizedRound;

    public ImmutableRound(
            List<GenericImmutableTrick> turns,
            IParmeterizedRound parmeterizedRound
    ) {
        this.turns = List.copyOf(turns);
        this.parmeterizedRound = parmeterizedRound;
    }

    public Map<IPlayerReference, Integer> getPointsByPlayer() {
        var pointsByTeam = new HashMap<IPlayerReference,Integer>();

        for(int i=0; i<turns.size();i++) {

            GenericImmutableTrick turn = turns.get(i);

            var countMode = parmeterizedRound.getCountMode(i);
            var rankMode = parmeterizedRound.getRankMode(i);

            pointsByTeam.merge( turn.whoTakes(rankMode),
                    turn.sum(countMode), Integer::sum );
        }
        return pointsByTeam;
    }

    public Map<ITeam, Integer> getPointsByTeam() {
        var pointsByTeam = new HashMap<ITeam,Integer>();

        for(int i=0; i<turns.size();i++) {

            GenericImmutableTrick turn = turns.get(i);

        var countMode = parmeterizedRound.getCountMode(i);
        var rankMode = parmeterizedRound.getRankMode(i);

        pointsByTeam.merge( turn.whoTakes(rankMode).getTeam(),
                        turn.sum(countMode), Integer::sum );
        }
        return pointsByTeam;
    }

    public List<ImmutableTrick> getParametrizedTurns() {
        return IntStream.range(0,turns.size())
                .mapToObj(i -> parametrized(turns.get(i),i))
                .collect(Collectors.toUnmodifiableList());
    }

    private ImmutableTrick parametrized(GenericImmutableTrick trick, int i) {
        IRankModeParametrized rankMode = parmeterizedRound.getRankMode(i);
        ICountModeParametrized countMode = parmeterizedRound.getCountMode(i);
        return new ImmutableTrick(
                trick.log,
                trick.whoTakes(rankMode),
                trick.sum(countMode),
                parmeterizedRound.getCaption(i)

        );
    }

    public Map<ITeam, Integer> getLastRoundBonus() {
        if (turns.size() == 9) {
            int lastIdx = turns.size() - 1;
            var rankMode = parmeterizedRound.getRankMode(lastIdx);
            GenericImmutableTrick lastTurn = turns.get(lastIdx);
            var team = lastTurn.whoTakes(rankMode).getTeam();
            return ImmutableMap.of(team, 5);
        } else {
            return ImmutableMap.of();
        }

    }

    public Map<ITeam, Integer> getTotalPointsByTeam() {
        Map<ITeam, Integer> map = getPointsByTeam();
        getLastRoundBonus().forEach((k, v) -> map.merge(k, v, Integer::sum));
        return parmeterizedRound.getCountMode(0).transformRoundResult(map);
    }
}
