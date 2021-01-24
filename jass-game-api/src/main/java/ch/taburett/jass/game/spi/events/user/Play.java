package ch.taburett.jass.game.spi.events.user;

import ch.taburett.jass.cards.JassCard;
import ch.taburett.jass.game.spi.events.IJassMessage;

public class Play implements IUserEvent<JassCard> {

    @Override
    public JassCard getPayload() {
        return payload;
    }

    private JassCard payload;

    public Play(JassCard payload) {
        this.payload = payload;
    }

    @Override
    public String getCode() {
        return "PLAY";
    }
}
