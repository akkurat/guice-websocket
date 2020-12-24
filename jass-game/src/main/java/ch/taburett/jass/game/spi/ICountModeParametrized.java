package ch.taburett.jass.game.spi;

import ch.taburett.jass.cards.JassCard;
import ch.taburett.jass.cards.JassValue;

import java.util.Map;

import static ch.taburett.jass.cards.JassValue.*;

public interface ICountModeParametrized {
    /**
     * Count Map for Current Round (Or whatever makes sense in the game mode)
     */

    Map<JassCard, Integer> getCountMap();

    Map<JassValue, Integer> commonCountMap = Map.of(
            _10, 10,
            _B, 2,
            _D, 3,
            _K, 4,
            _A, 11
    );


}
