package ch.taburett.jass.cards;

import java.util.Comparator;

public class JassCard {

    public final JassColor color;
    public final JassValue value;

    JassCard(JassColor color, JassValue value) {
        this.color = color;
        this.value = value;
    }

    @Override
    public String toString() {
        return "JassCard{" +
                "color=" + color +
                ", value=" + value +
                '}';
    }

    public static Comparator<JassCard> c() {
        return Comparator.comparingInt( (JassCard c) -> c.color.sort )
                .thenComparingInt( (JassCard c) -> c.value.rank);
    }
}
