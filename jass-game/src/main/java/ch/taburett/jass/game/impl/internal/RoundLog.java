package ch.taburett.jass.game.impl.internal;

import ch.taburett.jass.game.api.ITeam;
import ch.taburett.jass.game.pub.log.ImmutableRound;

import java.util.HashMap;
import java.util.List;

public class RoundLog {
    List<ImmutableRound> rounds;

    public RoundLog(List<ImmutableRound> rounds) {
        this.rounds = rounds;
    }

    HashMap<ITeam, Integer> getPoints()
    {
        var result = new HashMap<ITeam,Integer>();

        for( ImmutableRound r : rounds ) {
            var p = r.getTotalPointsByTeam();
            p.forEach((k,v) ->  {
                result.merge(k,v, Integer::sum);
            });
        }
        return result;
    }
}
