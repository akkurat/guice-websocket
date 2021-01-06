package ch.taburett.jass.game.api;

import ch.taburett.jass.game.spi.events.server.IServerMessage;
import ch.taburett.jass.game.spi.events.user.IUserEvent;

import java.util.function.Consumer;

public interface IPlayerReference {
    String getRef();


    void setProxy(Consumer<IServerMessage<?>> proxy);

    void sendToServer(IUserEvent<?> event);

    default ITeam getTeam() {
        return null;
    }

}
