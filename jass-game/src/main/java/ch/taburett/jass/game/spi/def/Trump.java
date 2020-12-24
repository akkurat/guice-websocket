package ch.taburett.jass.game.spi.def;

import ch.taburett.jass.cards.JassColor;
import ch.taburett.jass.game.spi.ICountModeParametrized;
import ch.taburett.jass.game.spi.IParmeterizedRound;

public class Trump implements IParmeterizedRound {

    private JassColor color;

    public Trump(JassColor herz) {
        this.color = herz;
    }

    public ICountModeParametrized getCountMode() {
        return new TrumpfCount(color);
    }

}