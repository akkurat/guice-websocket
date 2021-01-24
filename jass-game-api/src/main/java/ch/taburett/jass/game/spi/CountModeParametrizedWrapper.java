package ch.taburett.jass.game.spi;

import ch.taburett.jass.cards.JassCard;
import ch.taburett.jass.game.api.ITeam;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Map;

@JsonSerialize(as=ICountModeParametrized.class)
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
    public Map<ITeam, Integer> transformRoundResult(Map<ITeam, Integer> result) {
        return mode.transformRoundResult(result);
    }

    @Override
    public String getCaption() {
        return mode.getCaption();
    }
}
