package ch.taburett.jass.game.spi.events.user;

import ch.taburett.jass.game.spi.events.IJassMessage;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class DecideEvent implements IUserEvent<DecideEvent.DecideParams> {

    public final DecideParams payload;

    @JsonCreator(mode= JsonCreator.Mode.PROPERTIES)
    public DecideEvent(@JsonProperty("payload") DecideParams payload) {
        this.payload = payload;
    }

    @Override
    public String getCode() {
        return "DECIDE";
    }

    @Override
    public DecideParams getPayload() {
        return payload;
    }

    public static class DecideParams {
        public final String type;
        public final Map<String, String> params;

        @JsonCreator( mode = JsonCreator.Mode.PROPERTIES )
        public DecideParams(@JsonProperty("type") String type,
                            @JsonProperty("params") Map<String, String> params) {
            this.type = type;
            this.params = params;
        }
    }
}
