package ch.taburett.jass.game.spi.def;

import ch.taburett.jass.cards.DeckUtil;
import ch.taburett.jass.cards.JassCard;
import ch.taburett.jass.cards.JassColor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

class TrumpfCountTest {

    @Test
    public void testTotal() {
        var allCards = DeckUtil.getInstance().createDeck();

        var count = new TrumpfCount(JassColor.ECKEN);

        var total = allCards.stream()
                .mapToInt( c-> count.getCount(c))
                .sum();


        Assertions.assertEquals(152, total );

    }

    @Test
    public void testTotalViaMap() {
        var count = new TrumpfCount(JassColor.HERZ);
        var total = count.getCountMap().values().stream()
                .mapToInt(v->v)
                .sum();

        Assertions.assertEquals(152, total );
    }


}