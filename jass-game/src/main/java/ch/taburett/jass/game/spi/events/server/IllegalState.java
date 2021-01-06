package ch.taburett.jass.game.spi.events.server;

import ch.taburett.jass.game.spi.events.IJassMessage;
import ch.taburett.jass.game.spi.events.server.IServerMessage;

public class IllegalState implements IServerMessage<String> {
    private final String message;

    public IllegalState(String message ) {
        this.message = message;
    }

    @Override
    public String getCode() {
        return "ERROR";
    }

    @Override
    public String getPayload() {
        return message;
    }
}
