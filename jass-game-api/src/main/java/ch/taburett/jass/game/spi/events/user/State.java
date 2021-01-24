package ch.taburett.jass.game.spi.events.user;

import ch.taburett.jass.game.spi.events.IJassMessage;

/** Requests State of the Game */
public class State implements IUserEvent<Object> {
    @Override
    public String getCode() {
        return "STATE";
    }

    @Override
    public Object getPayload() {
        return null;
    }
}
