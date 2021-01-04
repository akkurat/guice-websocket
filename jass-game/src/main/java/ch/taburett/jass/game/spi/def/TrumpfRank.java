package ch.taburett.jass.game.spi.def;

import ch.taburett.jass.cards.JassCard;
import ch.taburett.jass.cards.JassColor;
import ch.taburett.jass.cards.JassValue;
import ch.taburett.jass.game.spi.IRankModeParametrized;

public class TrumpfRank implements IRankModeParametrized {

    public JassColor getTrumpf() {
        return trumpf;
    }

    private final JassColor trumpf;

    public TrumpfRank(JassColor trumpf) {
        this.trumpf = trumpf;
    }

    @Override
    public int getRank(JassCard c, JassColor roundColor) {
        if(c.color == trumpf ) {
            if(c.value == JassValue._B) {
                return JassValue.maxBaseRank * 2 + 2; // 30
            } else if( c.value == JassValue._9 ) {
                return JassValue.maxBaseRank * 2 + 1; // 29
            } else {
                return JassValue.maxBaseRank + c.value.rank; // 20...28
            }
        }
        if(roundColor == c.color) {
            return c.value.rank;
        }
        return 0;
    }

    @Override
    public String toString() {
        return "TrumpfRank{" +
                "trumpf=" + trumpf +
                '}';
    }
}
