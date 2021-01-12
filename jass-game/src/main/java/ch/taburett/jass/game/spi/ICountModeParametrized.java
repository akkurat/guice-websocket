package ch.taburett.jass.game.spi;

import ch.taburett.jass.cards.JassCard;
import ch.taburett.jass.cards.JassValue;
import ch.taburett.jass.game.api.ITeam;
import ch.taburett.jass.game.impl.internal.Team;
import com.google.common.collect.ImmutableMap;

import java.util.Map;

import static ch.taburett.jass.cards.JassValue.*;

public interface ICountModeParametrized {
    /**
     * Count Map for Current Round (Or whatever makes sense in the game mode)
     */

    Map<JassCard, Integer> getCountMap();

    Map<JassValue, Integer> commonCountMap = ImmutableMap.of(
            _10, 10,
            _B, 2,
            _D, 3,
            _K, 4,
            _A, 11
    );

    default Integer getCount(JassCard jc) {
        return getCountMap().get(jc);
    }

    default Map<ITeam, Integer> transformRoundResult( Map<ITeam,Integer> result ) {
        return result;
    }

    String getCaption();
}
