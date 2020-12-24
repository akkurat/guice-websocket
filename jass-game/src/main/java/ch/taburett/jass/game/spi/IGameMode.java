package ch.taburett.jass.game.spi;

public interface IGameMode {
    IRound createRound(IRound round);

    ICountModeParametrized getCountMode();
}
