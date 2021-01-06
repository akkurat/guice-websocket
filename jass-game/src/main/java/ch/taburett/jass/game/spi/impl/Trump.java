package ch.taburett.jass.game.spi.impl;

import ch.taburett.jass.cards.JassColor;
import ch.taburett.jass.game.spi.ICountModeParametrized;
import ch.taburett.jass.game.spi.IParmeterizedRound;
import ch.taburett.jass.game.spi.IRankModeParametrized;

public class Trump implements IParmeterizedRound {

    private JassColor color;


    private int faktor;

    public Trump(JassColor herz, int faktor) {
        this.color = herz;
        this.faktor = faktor;
    }

    public ICountModeParametrized getCountMode() {
        return new TrumpfCount(color);
    }

    @Override
    public IRankModeParametrized getRankMode() {
        return new TrumpfRank(color);
    }

    @Override
    public int getFactor() {
        return faktor;
    }

    @Override
    public String toString() {
        return "Trump{" +
                "color=" + color +
                ", faktor=" + faktor +
                '}';
    }
}