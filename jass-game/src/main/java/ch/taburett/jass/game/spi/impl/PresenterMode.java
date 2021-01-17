package ch.taburett.jass.game.spi.impl;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class PresenterMode {
    private final String description;
    private final IModeFactory modeFactory;

    public String getDescription() {
        return description;
    }

    @JsonIgnore
    public IModeFactory getModeFactory() {
        return modeFactory;
    }

    public PresenterMode(String description, IModeFactory modeFactory) {
        this.description = description;
        this.modeFactory = modeFactory;
    }



}

