package ch.taburett.jass.game.spi;

import ch.taburett.jass.cards.JassCard;
import ch.taburett.jass.game.Team;

import java.util.Map;

public class CountModeParametrizedWrapper implements ICountModeParametrized {
    private final ICountModeParametrized mode;

    public CountModeParametrizedWrapper(ICountModeParametrized mode) {

        this.mode = mode;
    }

    @Override
    public Map<JassCard, Integer> getCountMap() {
        return mode.getCountMap();
    }

    @Override
    public Integer getCount(JassCard jc) {
        return mode.getCount(jc);
    }

    @Override
    public Map<Team, Integer> transformRoundResult(Map<Team, Integer> result) {
        return mode.transformRoundResult(result);
    }
}
