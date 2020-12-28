package ch.taburett.jass.game.spi;

import ch.taburett.jass.game.Game;

public interface IStichMode {
    Game.PlayerReference winner(IRound round);
}
