package ch.taburett.jass.game.spi.impl;

import ch.taburett.jass.cards.DeckUtil;
import ch.taburett.jass.cards.JassCard;
import ch.taburett.jass.cards.JassColor;
import ch.taburett.jass.cards.JassValue;
import ch.taburett.jass.game.spi.ICountModeParametrized;
import ch.taburett.jass.game.spi.IParmeterizedRound;
import ch.taburett.jass.game.spi.IRankModeParametrized;
import lombok.Data;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class Undenufe implements IParmeterizedRound {

    private final int factor;
    private final UndenufeRank obenAbe;
    private final UndenufePoints undePoints;

    public Undenufe(int factor) {
        this.factor = factor;
        obenAbe = new UndenufeRank();
        undePoints  = new UndenufePoints();
    }

    @Override
    public ICountModeParametrized getCountMode(int round) {
        return undePoints;
    }

    @Override
    public IRankModeParametrized getRankMode(int round) {
        return obenAbe;
    }

    @Override
    public String getCaption() {
        return "Undenufe";
    }

    public static class UndenufeRank implements IRankModeParametrized {

        public int getRank(JassCard c, JassColor roundColor) {
            int baseRank = c.value.rank;
            if (roundColor == c.color) {
                return -baseRank;
            }
            return -JassValue.maxBaseRank-1;
        }

        @Override
        public Comparator<? super JassCard> getComparator(JassColor roundColor) {
            return Comparator.comparingInt(c -> getRank(c, roundColor));
        }

        @Override
        public List<JassCard> legalCards(List<JassCard> trick, List<JassCard> hand) {
            return LeihUtil.legalCards_(trick, hand);
        }

        @Override
        public String getCaption() {
            return "Undenufe";
        }
    }

    public static class UndenufePoints implements ICountModeParametrized{
        private final Map<JassCard, Integer> valueMap;

        public UndenufePoints() {
            this.valueMap = DeckUtil.getInstance().createDeck().stream()
                    .collect(Collectors.toMap(c -> c, this::mapCardValue));
        }

        private int mapCardValue(JassCard c) {
            JassValue v = c.value;
            if (v == JassValue._8) {
                return 8;
            }
            if (v == JassValue._A) {
                return 0;
            }
            if (v == JassValue._6) {
                return 11;
            }

            Integer count = commonCountMap.get(v);
            return count != null ? count : 0;
        }


        @Override
        public Map<JassCard, Integer> getCountMap() {
            return valueMap;
        }

        @Override
        public String getCaption() {
            return "Undenufe";
        }
    }
}

