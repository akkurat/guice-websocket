package ch.taburett.jass.game.spi.events.server;

import ch.taburett.jass.game.impl.PlayerReference;
import lombok.Data;

@Data
public class IllegalState implements IServerMessage<String> {
    private final PlayerReference to;
    private final String payload;


    @Override
    public String getCode() {
        return "ERROR";
    }
}
