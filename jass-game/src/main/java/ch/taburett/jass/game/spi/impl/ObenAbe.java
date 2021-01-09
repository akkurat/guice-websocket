package ch.taburett.jass.game.spi.impl;

import ch.taburett.jass.cards.DeckUtil;
import ch.taburett.jass.cards.JassCard;
import ch.taburett.jass.cards.JassColor;
import ch.taburett.jass.cards.JassValue;
import ch.taburett.jass.game.spi.ICountModeParametrized;
import ch.taburett.jass.game.spi.IParmeterizedRound;
import ch.taburett.jass.game.spi.IRankModeParametrized;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Map;
import java.util.stream.Collectors;

public class ObenAbe implements IParmeterizedRound {

    private final ObenAbeMode mode;
    private int faktor;

    ObenAbe() {
        this(1);
    }

    ObenAbe(int faktor) {
        this.faktor = faktor;
        this.mode = new ObenAbeMode();
    }
    @Override
    public int getFactor() {
        return faktor;
    }

    @Override
    public ICountModeParametrized getCountMode() {
        return mode;
    }

    @Override
    public IRankModeParametrized getRankMode() {
        return mode;
    }

    @Override
    public String getCaption() {
        return "Oben-Abe " + faktor + "x";
    }

    public static class ObenAbeMode implements IRankModeParametrized, ICountModeParametrized{
        private final Map<JassCard, Integer> valueMap;

        public ObenAbeMode() {
            this.valueMap = DeckUtil.getInstance().createDeck().stream()
                    .collect(Collectors.toMap(c -> c, this::mapCardValue));
        }

        private int mapCardValue(JassCard c) {
        JassValue v = c.value;
        if (v == JassValue._8) {
            return 8;
        }
        Integer count = commonCountMap.get(v);
        return count != null ? count : 0;
    }


    @Override
    public Map<JassCard, Integer> getCountMap() {
        return valueMap;
    }


    @Override
    public int getRank(JassCard c, JassColor roundColor) {
        int baseRank = c.value.rank;
        if (roundColor == c.color) {
            return baseRank;
        }
        return 0;
    }
    }
}