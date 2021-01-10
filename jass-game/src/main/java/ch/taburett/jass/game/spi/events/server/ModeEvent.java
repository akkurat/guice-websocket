package ch.taburett.jass.game.spi.events.server;

import ch.taburett.jass.cards.JassCard;
import ch.taburett.jass.game.impl.PlayerReference;
import ch.taburett.jass.game.spi.impl.PresenterMode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class ModeEvent implements IServerMessage<ModeEvent.ModePayload> {

    public final PlayerReference to;
    public final ModePayload payload;

    @Override
    public String getCode() {
        return "MODE";
    }


    public static class ModePayload {
        public final Map<String, PresenterMode> modes;
        public final List<JassCard> cards;

        public ModePayload(
                Map<String, PresenterMode> modes,
                List<JassCard> cards,
                PlayerReference who
        ) {
            this.modes = modes;
            this.cards = cards.stream()
                    .sorted(JassCard.c())
                    .collect(Collectors.toList());
        }
    }
}
