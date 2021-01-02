package ch.taburett.jass.game.spi.messages;

public class Status  implements IJassMessage<StatusPayload> {
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
