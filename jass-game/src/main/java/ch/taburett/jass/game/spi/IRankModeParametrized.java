package ch.taburett.jass.game.spi;

import ch.taburett.jass.cards.JassCard;
import ch.taburett.jass.cards.JassColor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import static java.util.stream.Collectors.toMap;

public interface IRankModeParametrized {
    /**
     * Count Map for Current Round (Or whatever makes sense in the game mode)
     */

    default JassCard getMaxCard(List<JassCard> cards) {
        JassColor roundColor = cards.get(0).color;
        return getMaxCard(roundColor, cards);
    }

    /**
     * Rank is context sensitive
     * hence we need the at least the color
     * of the round
     * @param cards
     * @return
     */
    default JassCard getMaxCard(JassColor roundColor, List<JassCard> cards) {
        return cards.stream().max(getComparator(roundColor)).orElseThrow();
    }

    default Comparator<? super JassCard> getComparator(JassColor roundColor) {
        return Comparator.comparingInt(c -> getRank(c,roundColor));
    }

    int getRank(JassCard c, JassColor roundColor);

    default List<JassCard> legalCards(List<JassCard> trick, List<JassCard> hand) {
        return hand;
    };
}
