package ch.taburett.jass.game.spi.events.user;

import ch.taburett.jass.game.spi.events.IJassMessage;

public class PassOnEvent implements IUserEvent<Object> {
    @Override
    public String getCode() {
        return "PASSON";
    }

    @Override
    public Object getPayload() {
        return null;
    }
}
