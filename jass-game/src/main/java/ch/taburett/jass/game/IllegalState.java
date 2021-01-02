package ch.taburett.jass.game;

import ch.taburett.jass.game.spi.messages.IJassMessage;

public class IllegalState implements IJassMessage<String> {
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
