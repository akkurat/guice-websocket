package ch.taburett.jass.game.spi.def;

import ch.taburett.jass.cards.DeckUtil;
import ch.taburett.jass.cards.JassCard;
import ch.taburett.jass.cards.JassValue;
import ch.taburett.jass.game.spi.ICountModeParametrized;

import java.util.Map;
import java.util.stream.Collectors;

public class ObenAbeCount implements ICountModeParametrized {

    private final Map<JassCard, Integer> map;

    ObenAbeCount()
    {
        this.map = DeckUtil.createDeck().stream()
                .collect(Collectors.toMap( c -> c, this::mapCard ));
    }

    private int mapCard(JassCard c) {
        JassValue v = c.getValue();
        if(v == JassValue._8) {
            return 8;
        }
        Integer count = commonCountMap.get(v);
        return count != null ? count : 0;
    }

    @Override
    public Map<JassCard, Integer> getCountMap() {
        return map;
    }

}
