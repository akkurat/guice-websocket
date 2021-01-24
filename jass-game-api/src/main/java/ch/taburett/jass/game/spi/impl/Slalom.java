package ch.taburett.jass.game.spi.impl;

import ch.taburett.jass.game.spi.ICountModeParametrized;
import ch.taburett.jass.game.spi.IParmeterizedRound;
import ch.taburett.jass.game.spi.IRankModeParametrized;

public class Slalom implements IParmeterizedRound {
    private final int factor;
    private final ObenAbe obe;
    private final Undenufe unde;
    private final EStart start;

    public Slalom(int factor, EStart start) {
        this.factor = factor;
        obe = new ObenAbe(factor);
        unde = new Undenufe(factor);
        this.start = start;
    }

    @Override
    public ICountModeParametrized getCountMode(int round) {
        return start == EStart.UNDE ? unde.getCountMode(round) : obe.getCountMode(round);
    }

    @Override
    public IRankModeParametrized getRankMode(int round) {
        return start.byRound(round) == EStart.UNDE ? unde.getRankMode(round) : obe.getRankMode(round);
    }

    @Override
    public int getFactor() {
        return factor;
    }

    @Override
    public String getCaption(int trick) {
        return start.byRound(trick).toString();
    }
    @Override
    public String getCaption() {
        return "Slalom " + start;
    }
    public enum EStart {
        OBE{
            @Override
            EStart byRound(int round) {
                return round % 2 == 0 ? OBE : UNDE;
            }
        },
        UNDE {
            @Override
            EStart byRound(int round) {
                return round % 2 == 0 ? UNDE : OBE;
            }
        };
        abstract EStart byRound(int round);
    }

}
