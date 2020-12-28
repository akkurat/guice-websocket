package ch.taburett.jass.game.spi;

import ch.taburett.jass.game.spi.def.PresenterMode;

import java.util.List;
import java.util.Map;

public interface IRoundSupplier {
    Map<String, PresenterMode> getModes(List<IRound> rounds);


}
