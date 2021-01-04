package ch.taburett.jass.game;

import java.util.HashMap;
import java.util.List;

public class RoundLog {
    List<ImmutableRound> rounds;

    public RoundLog(List<ImmutableRound> rounds) {
        this.rounds = rounds;
    }

    HashMap<Team, Integer> getPoints()
    {
        var result = new HashMap<Team,Integer>();

        for( ImmutableRound r : rounds ) {
            var p = r.getTotalPointsByTeam();
            p.forEach((k,v) ->  {
                result.merge(k,v, Integer::sum);
            });
        }
        return result;
    }
}
