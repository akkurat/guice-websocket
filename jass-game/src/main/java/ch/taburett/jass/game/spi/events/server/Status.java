package ch.taburett.jass.game.spi.events.server;

import ch.taburett.jass.game.impl.PlayerReference;
import ch.taburett.jass.game.spi.events.IJassMessage;
import lombok.Data;

@Data
public class Status  implements IServerMessage<StatusPayload> {
    private final PlayerReference to;
    private final StatusPayload payload;

    @Override
    public String getCode() {
        return "STATUS";
    }

    @Override
    public int getBuffertype() {
        return 2;
    }
}
