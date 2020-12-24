package ch.taburett.jass.game;

import ch.taburett.jass.cards.JassCard;
import ch.taburett.jass.game.spi.messages.IJassMessage;

import java.util.List;

public record DistributeEvent(PlayerReference getFrom, PlayerReference getTo,
                    List<JassCard> getPayload) implements IJassMessage<List<JassCard>>
{
    @Override
    public String getCode() {
        return "DIST";
    }


}
