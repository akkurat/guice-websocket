package ch.taburett.jass.game.spi;

import ch.taburett.jass.game.impl.internal.Trick;
import com.google.common.collect.ImmutableMap;

import java.util.Map;

public interface IParmeterizedRound {
    ICountModeParametrized getCountMode(int trick);

    IRankModeParametrized getRankMode(int trick);

    int getFactor();

    String getCaption();

    default String getCaption(int trick) {
        return getCaption();
    }

    default Map<String,Object> getSemanticInfo() {
        return ImmutableMap.of();
    }

    default TrickMode getTrickMode(int trick) {
        return new TrickMode(getCaption(), getCaption(trick), getFactor());
    }

}
