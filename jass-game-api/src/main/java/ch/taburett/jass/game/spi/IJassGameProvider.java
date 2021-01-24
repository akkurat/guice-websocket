package ch.taburett.jass.game.spi;

import java.util.Collection;

public interface IJassGameProvider {
    Collection<IRoundSupplier> getRoundSuppliers();
}
