package ch.taburett.jass.game.spi.def;

import ch.taburett.jass.game.spi.IParmeterizedRound;

import java.util.function.Supplier;

public class PresenterMode {
    private final String description;
    private final Supplier<IParmeterizedRound> modeFactory;

    public PresenterMode(String description, Supplier<IParmeterizedRound> modeFactory) {
        this.description = description;
        this.modeFactory = modeFactory;
    }

}

