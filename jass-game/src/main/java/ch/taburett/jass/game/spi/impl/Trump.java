package ch.taburett.jass.game.spi.impl;

import ch.taburett.jass.cards.JassColor;
import ch.taburett.jass.game.spi.ICountModeParametrized;
import ch.taburett.jass.game.spi.IParmeterizedRound;
import ch.taburett.jass.game.spi.IRankModeParametrized;

public class Trump implements IParmeterizedRound {

    private final TrumpfCount trumpfCount;
    private final TrumpfRank trumpfRank;
    private final JassColor color;

    private int faktor;

    public Trump(JassColor herz, int faktor) {
        this.color = herz;
        this.faktor = faktor;
        trumpfCount = new TrumpfCount(color);
        trumpfRank = new TrumpfRank(color);
    }

    @Override
    public ICountModeParametrized getCountMode() {
        return trumpfCount;
    }

    @Override
    public IRankModeParametrized getRankMode() {
        return trumpfRank;
    }

    @Override
    public int getFactor() {
        return faktor;
    }

    @Override
    public String getCaption() {
        return color+" Trumpf " + faktor + "x";
    }

    @Override
    public String toString() {
        return "Trump{" +
                "color=" + color +
                ", faktor=" + faktor +
                '}';
    }
}