package ch.taburett.jass.game.spi;

import ch.taburett.jass.game.PlayerReference;

public interface IStichMode {
    PlayerReference winner(IRound round);
}
