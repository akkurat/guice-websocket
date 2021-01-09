package ch.taburett.jass.game.spi.impl;

import ch.taburett.jass.game.api.ITeam;
import ch.taburett.jass.game.spi.CountModeParametrizedWrapper;
import ch.taburett.jass.game.spi.ICountModeParametrized;
import ch.taburett.jass.game.spi.IParmeterizedRound;
import ch.taburett.jass.game.spi.IRankModeParametrized;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Iterator;
import java.util.Map;

@JsonSerialize(as=IParmeterizedRound.class)
public class Misere implements IParmeterizedRound {

    private int faktor;

    public Misere(int faktor) {
        this.faktor = faktor;
    }

    @Override
    public ICountModeParametrized getCountMode() {
        return new MisereCount();
    }

    @Override
    public IRankModeParametrized getRankMode() {
        return new ObenAbe.ObenAbeMode();
    }

    @Override
    public int getFactor() {
        return faktor;
    }

    @Override
    public String getCaption() {
        return "Misere";
    }

    public static class MisereCount extends CountModeParametrizedWrapper {

        public MisereCount() {
            super(new ObenAbe.ObenAbeMode());
        }

        @Override
        public Map<ITeam, Integer> transformRoundResult(Map<ITeam, Integer> result) {

            if( result.size() == 2 ) {

                Iterator<Map.Entry<ITeam, Integer>> iterator = result.entrySet().iterator();
                var first = iterator.next();
                var other = iterator.next();

                return Map.of(first.getKey(), other.getValue(),
                        other.getKey(), first.getValue());
            }
            return result;
        }
    }
}