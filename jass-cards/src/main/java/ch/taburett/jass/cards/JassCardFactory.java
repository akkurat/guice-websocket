package ch.taburett.jass.cards;

import java.util.concurrent.ConcurrentHashMap;

/**
 * To ease the use with List.remove
 * and enable '==' equality
 */
public class JassCardFactory {
    ConcurrentHashMap<String, JassCard> cards = new ConcurrentHashMap<>();
    JassCard getJassCard(JassColor c, JassValue v) {
        var key = getKey(c,v);
        return cards.computeIfAbsent( key, k -> new JassCard(c,v));
    }

    private String getKey(JassColor c, JassValue v) {
        return c.name()+v.name();
    }


}
