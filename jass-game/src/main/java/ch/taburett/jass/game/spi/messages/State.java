package ch.taburett.jass.game.spi.messages;

public class State implements IJassMessage{
    @Override
    public String getCode() {
        return "STATE";
    }

    @Override
    public Object getPayload() {
        return null;
    }
}
