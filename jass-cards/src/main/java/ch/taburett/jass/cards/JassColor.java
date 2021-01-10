package ch.taburett.jass.cards;
public enum JassColor {
    HERZ(1),
    ECKEN(3),
    SCHAUFEL(2),
    KREUZ(4);

    public final int sort;

    JassColor(int i) {
        this.sort = i;
    }
}
