package ch.taburett.jass.game.spi;

import ch.taburett.jass.game.api.IPlayerReference;

public interface IStichMode {
    IPlayerReference winner(IRound round);
}
