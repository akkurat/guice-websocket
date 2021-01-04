package ch.taburett.jass.cards;

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
}
