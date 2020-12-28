package ch.taburett.jass.game;

import ch.taburett.jass.game.spi.messages.IJassMessage;

public record ChatMessage(Game.PlayerReference getTo, Game.PlayerReference getFrom, String getPayload) implements IJassMessage<String> {
    @Override
    public String getCode() {
        return "CHAT";
    }
}
