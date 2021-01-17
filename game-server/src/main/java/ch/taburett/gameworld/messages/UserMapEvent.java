package ch.taburett.gameworld.messages;

import ch.taburett.jass.game.api.IPlayerReference;
import ch.taburett.jass.game.spi.events.server.IServerMessage;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class UserMapEvent implements IServerMessage<UserMapEvent.UserPayload> {

    private final IPlayerReference to;
    private final UserPayload payload;

    @Override
    public String getCode() {
        return "USERS";
    }

    @Override
    public int getBuffertype() {
        return 1;
    }

    @Data
    public static class UserPayload {
        private final List<IPlayerReference> playerList;
        private final Map<String,String> users;
        private final String youAre;
    }

}
