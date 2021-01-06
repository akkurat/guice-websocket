package ch.taburett.jass.game.spi.impl;

import ch.taburett.jass.game.spi.IParmeterizedRound;

import java.util.Map;
import java.util.function.Function;

public class PresenterMode {
    private final String description;
    private final Function<Map<String,String>,IParmeterizedRound> modeFactory;

    public String getDescription() {
        return description;
    }

    public Function<Map<String,String>,IParmeterizedRound> getModeFactory() {
        return modeFactory;
    }

    public PresenterMode(String description, Function<Map<String, String>, IParmeterizedRound> modeFactory) {
        this.description = description;
        this.modeFactory = modeFactory;
    }



}

