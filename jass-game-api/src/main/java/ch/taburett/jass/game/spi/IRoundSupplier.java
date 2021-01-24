package ch.taburett.jass.game.spi;

import ch.taburett.jass.game.pub.log.ImmutableRound;
import ch.taburett.jass.game.spi.impl.PresenterMode;

import java.util.ArrayList;
import java.util.Map;

public interface IRoundSupplier {
    Map<String, PresenterMode> getModes(ArrayList<ImmutableRound> rounds);

    int getMaxPoints();
}