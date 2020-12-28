package ch.taburett.jass.game.spi.messages;

import ch.taburett.jass.cards.JassCard;

import java.util.List;

public record YourTurn(Payload getPayload) implements IJassMessage<YourTurn.Payload> {

    @Override
    public String getCode() {
        return "TURN";
    }

    public record Payload(List<JassCard> getAvailCards, List<JassCard> getRoundCards){
    }

}
