package ch.taburett.jass.game.spi.def;

import ch.taburett.jass.game.spi.ICountModeParametrized;
import ch.taburett.jass.game.spi.IParmeterizedRound;

public class Misere implements IParmeterizedRound {

    public ICountModeParametrized getCountMode() {
        return new ObenAbeCount();
    }

}