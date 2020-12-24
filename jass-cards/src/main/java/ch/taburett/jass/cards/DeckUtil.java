package ch.taburett.jass.cards;

import java.util.HashSet;
import java.util.Set;

public class DeckUtil {
    public static Set<JassCard> createDeck() {
        HashSet<JassCard> cards = new HashSet<>();
        for( JassColor c : JassColor.values() )
        {
            for( JassValue v : JassValue.values() )
            {
                cards.add( new JassCard( c, v) );
            }
        }
        return cards;
    }
}
