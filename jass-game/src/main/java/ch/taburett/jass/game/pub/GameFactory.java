package ch.taburett.jass.game.pub;

import ch.taburett.jass.game.api.IGame;
import ch.taburett.jass.game.impl.internal.Game;
import ch.taburett.jass.game.spi.IRoundSupplier;

public class GameFactory {
    public static IGame create(IRoundSupplier supplier) {
        return new Game(supplier);
    }
}
