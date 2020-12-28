package ch.taburett.jass.game.spi.messages;

import ch.taburett.jass.cards.JassCard;

public record Play(JassCard getPayload) implements IJassMessage<JassCard> {
    @Override
    public String getCode() {
        return "PLAY";
    }
}
