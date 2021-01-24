package ch.taburett.jass.game.spi.events.server;

import ch.taburett.jass.game.api.IPlayerReference;
import lombok.Data;

@Data
public class IllegalState implements IServerMessage<String> {
    private final IPlayerReference to;
    private final String payload;


    @Override
    public String getCode() {
        return "ERROR";
    }

    @Override
    public int getBuffertype() {
        return 0;
    }
}
