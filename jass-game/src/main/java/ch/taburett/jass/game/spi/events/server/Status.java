package ch.taburett.jass.game.spi.events.server;

import ch.taburett.jass.game.spi.events.IJassMessage;

public class Status  implements IServerMessage<StatusPayload> {
    private StatusPayload payload;

    public Status(StatusPayload payload) {
        this.payload = payload;
    }

    @Override
    public String getCode() {
        return "STATUS";
    }


    @Override
    public StatusPayload getPayload() {
        return payload;
    }
}
