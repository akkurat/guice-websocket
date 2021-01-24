package ch.taburett.jass.game.spi.events.server;

import ch.taburett.jass.game.api.IPlayerReference;
import lombok.Data;

@Data
public class Status  implements IServerMessage<StatusPayload> {
    private final IPlayerReference to;
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
