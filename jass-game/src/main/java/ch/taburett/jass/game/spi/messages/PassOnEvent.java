package ch.taburett.jass.game.spi.messages;

public class PassOnEvent implements IJassMessage {
    @Override
    public String getCode() {
        return "PASSON";
    }

    @Override
    public Object getPayload() {
        return null;
    }
}
