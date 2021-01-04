package ch.taburett.jass.game;

import ch.taburett.jass.game.spi.messages.IJassMessage;

public class ChatMessage implements IJassMessage<String> {
    @Override
    public PlayerReference getTo() {
        return to;
    }

    @Override
    public PlayerReference getFrom() {
        return from;
    }

    @Override
    public String getPayload() {
        return payload;
    }

    private final PlayerReference to;
    private final PlayerReference from;
    private final String payload;

    public ChatMessage(PlayerReference to, PlayerReference from, String payload) {

        this.to = to;
        this.from = from;
        this.payload = payload;
    }

    @Override
    public String getCode() {
        return "CHAT";
    }
}
