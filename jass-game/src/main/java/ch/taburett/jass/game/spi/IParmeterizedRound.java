package ch.taburett.jass.game.spi;

public interface IParmeterizedRound {
    ICountModeParametrized getCountMode();

    IRankModeParametrized getRankMode();

    int getFactor();
}
