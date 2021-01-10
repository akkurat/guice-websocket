package ch.taburett.gameworld.services;

import ch.taburett.jass.game.api.IPlayerReference;
import ch.taburett.jass.game.spi.events.server.IServerMessage;
import lombok.Data;

import java.util.Map;

@Data
public class UserMapEvent implements IServerMessage<UserMapEvent.UserPayload> {

    private final IPlayerReference to;
    private final UserPayload payload;

    @Override
    public String getCode() {
        return "USERS";
    }

    @Data
    public static class UserPayload {
        private final Map<String,String> users;
        private final String youAre;
    }

}
