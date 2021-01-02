package ch.taburett.jass.game;

import ch.taburett.jass.game.spi.messages.IJassMessage;

public class ChatMessage implements IJassMessage<String> {
    @Override
    public Game.PlayerReference getTo() {
        return to;
    }

    @Override
    public Game.PlayerReference getFrom() {
        return from;
    }

    @Override
    public String getPayload() {
        return payload;
    }

    private final Game.PlayerReference to;
    private final Game.PlayerReference from;
    private final String payload;

    public ChatMessage(Game.PlayerReference to, Game.PlayerReference from, String payload) {

        this.to = to;
        this.from = from;
        this.payload = payload;
    }

    @Override
    public String getCode() {
        return "CHAT";
    }
}
