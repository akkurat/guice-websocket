package ch.taburett.jass.game.spi.messages;

import ch.taburett.jass.cards.JassCard;

public class Play implements IJassMessage<JassCard> {

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
