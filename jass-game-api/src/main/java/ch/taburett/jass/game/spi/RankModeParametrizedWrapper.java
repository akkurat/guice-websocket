package ch.taburett.jass.game.spi;

import ch.taburett.jass.cards.JassCard;
import ch.taburett.jass.cards.JassColor;

import java.util.Comparator;
import java.util.List;

public class RankModeParametrizedWrapper implements IRankModeParametrized {
    protected IRankModeParametrized mode;

    @Override
    public JassCard getMaxCard(List<JassCard> cards) {
        return mode.getMaxCard(cards);
    }

    @Override
    public JassCard getMaxCard(JassColor roundColor, List<JassCard> cards) {
        return mode.getMaxCard(roundColor, cards);
    }

    @Override
    public Comparator<? super JassCard> getComparator(JassColor roundColor) {
        return mode.getComparator(roundColor);
    }

    @Override
    public String getCaption() {
        return mode.getCaption();
    }

    public RankModeParametrizedWrapper(IRankModeParametrized mode ) {
        this.mode = mode;
    }
}
