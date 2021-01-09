package ch.taburett.jass.game.spi;

import java.util.Map;

public interface IParmeterizedRound {
    ICountModeParametrized getCountMode();

    IRankModeParametrized getRankMode();

    int getFactor();

    String getCaption();

    default Map<String,Object> getSemanticInfo() {
        return Map.of();
    }
}
