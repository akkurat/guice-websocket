package ch.taburett.jass.game.spi.def;

import ch.taburett.jass.cards.DeckUtil;
import ch.taburett.jass.cards.JassCard;
import ch.taburett.jass.cards.JassColor;
import ch.taburett.jass.cards.JassValue;
import ch.taburett.jass.game.spi.ICountModeParametrized;

import java.util.Map;
import java.util.stream.Collectors;

public class TrumpfCount implements ICountModeParametrized {

    private final Map<JassCard, Integer> map;

    TrumpfCount( JassColor color )
    {
        this.map = DeckUtil.createDeck().stream()
                .collect(Collectors.toMap( c -> c, c -> mapCard(c, color)));
    }

    private int mapCard( JassCard c, JassColor color ) {
        JassValue v = c.getValue();
        if( c.getColor() == color ) {
            if (v == JassValue._9) {
                return 14;
            }
            if ( v == JassValue._B)
            {
                return 20;
            }
        }
        Integer count = commonCountMap.get(v);
        return count != null ? count : 0;
    }

    @Override
    public Map<JassCard, Integer> getCountMap() {
        return null;
    }

}
