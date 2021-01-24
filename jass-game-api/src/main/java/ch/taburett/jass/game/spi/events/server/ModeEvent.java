package ch.taburett.jass.game.spi.events.server;

import ch.taburett.jass.cards.JassCard;
import ch.taburett.jass.game.api.IPlayerReference;
import ch.taburett.jass.game.spi.impl.PresenterMode;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class ModeEvent implements IServerMessage<ModeEvent.ModePayload> {

    public final IPlayerReference to;
    public final ModePayload payload;

    @Override
    public String getCode() {
        return "MODE";
    }

    @Override
    public int getBuffertype() {
        return 2;
    }


    public static class ModePayload {
        public final Map<String, PresenterMode> modes;
        public final List<JassCard> cards;

        public ModePayload(
                Map<String, PresenterMode> modes,
                List<JassCard> cards,
                IPlayerReference who
        ) {
            this.modes = modes;
            this.cards = cards.stream()
                    .sorted(JassCard.c())
                    .collect(Collectors.toList());
        }
    }
}
