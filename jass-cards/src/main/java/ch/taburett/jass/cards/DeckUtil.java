package ch.taburett.jass.cards;

import java.util.HashSet;
import java.util.Set;

public class DeckUtil {

    private final JassCardFactory f;

    private DeckUtil(JassCardFactory jassCardFactory) {
        this.f = jassCardFactory;
    }

    /**
     * This could be theoretically be solved via DI.
     * However having Spring Dependency Injection as a
     * Dependency seems like an overkill
     */

    private static final DeckUtil INSTANCE = new DeckUtil(new JassCardFactory());
    public static DeckUtil getInstance() {
        return INSTANCE;
    }

    public Set<JassCard> createDeck() {
        HashSet<JassCard> cards = new HashSet<>();
        for( JassColor c : JassColor.values() )
        {
            for( JassValue v : JassValue.values() )
            {
                cards.add( f.getJassCard( c, v) );
            }
        }
        return cards;
    }

    public JassCard getJassCard(JassColor c, JassValue v) {
        return f.getJassCard(c, v);
    }
}
