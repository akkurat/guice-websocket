package ch.taburett.jass.game.spi.impl;

import ch.taburett.jass.game.api.ITeam;
import ch.taburett.jass.game.impl.internal.Team;
import ch.taburett.jass.game.spi.CountModeParametrizedWrapper;
import ch.taburett.jass.game.spi.ICountModeParametrized;
import ch.taburett.jass.game.spi.IParmeterizedRound;
import ch.taburett.jass.game.spi.IRankModeParametrized;

import java.util.Iterator;
import java.util.Map;

public class Misere implements IParmeterizedRound {

    private int faktor;

    public Misere(int faktor) {
        this.faktor = faktor;
    }

    @Override
    public ICountModeParametrized getCountMode() {
        return new ObenAbeCount();
    }

    @Override
    public IRankModeParametrized getRankMode() {
        return new ObenAbeCount();
    }

    @Override
    public int getFactor() {
        return faktor;
    }

    public static class MisereCount extends CountModeParametrizedWrapper {


        public MisereCount() {
            super(new ObenAbeCount());
        }

        @Override
        public Map<ITeam, Integer> transformRoundResult(Map<ITeam, Integer> result) {
            assert result.size() == 2;

            Iterator<Map.Entry<ITeam, Integer>> iterator = result.entrySet().iterator();
            var first = iterator.next();
            var other = iterator.next();

            return Map.of(first.getKey(), other.getValue(),
                    other.getKey(), first.getValue());
        }
    }
}