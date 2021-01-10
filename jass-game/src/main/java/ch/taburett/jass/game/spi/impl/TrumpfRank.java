package ch.taburett.jass.game.spi.impl;

import ch.taburett.jass.cards.*;
import ch.taburett.jass.game.spi.IRankModeParametrized;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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

    @Override
    public List<JassCard> legalCards(List<JassCard> trick, List<JassCard> hand) {



        if(trick.isEmpty()) {
            return hand;
        }
        JassCard trumpfbube = DeckUtil.getInstance().getJassCard(trumpf, JassValue._B);
        JassColor color = trick.get(0).color;
        List<JassCard> handByColor = new ArrayList<>(ObenAbe.ObenAbeMode.legalCards_(trick, hand));
        if(color != trumpf) {
            handByColor.addAll(hand.stream().filter(c -> c.color==trumpf).collect(Collectors.toList()));
        }
        if( handByColor.size() == 1 && handByColor.get(0) == trumpfbube ) {
            return hand;
        }
        return handByColor;
    }
}
