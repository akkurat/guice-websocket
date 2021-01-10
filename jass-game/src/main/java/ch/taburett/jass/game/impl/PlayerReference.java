package ch.taburett.jass.game.impl;

import ch.taburett.jass.game.impl.internal.Game;
import ch.taburett.jass.game.impl.internal.Team;
import ch.taburett.jass.game.api.IPlayerReference;
import ch.taburett.jass.game.spi.events.server.IServerMessage;
import ch.taburett.jass.game.spi.events.user.IUserEvent;
import lombok.EqualsAndHashCode;

import java.util.function.Consumer;

@EqualsAndHashCode
public class PlayerReference implements IPlayerReference {

    private Game game;
    private final String ref;
    private Team team;
    @EqualsAndHashCode.Exclude
    private Consumer<IServerMessage<?>> proxy;

    @Override
    public String getRef() {
        return ref;
    }


    public PlayerReference(Game game, String ref, Team team) {
        this.ref = ref;
        this.team = team;
        this.game = game;
    }

    @Override
    public void setProxy(Consumer<IServerMessage<?>> proxy) {
        this.proxy = proxy;
    }

    public void sendToUser(IServerMessage<?> event) {
        if (proxy == null) {
            throw new IllegalStateException("Proxy must always be present");
        }
        proxy.accept(event);
    }

    @Override
    public void sendToServer(IUserEvent<?> event) {
        game.accept(event, this);
    }


    public Team getTeam() {
        return this.team;
    }

    @Override
    public String toString() {
        return "PlayerReference{" +
                "ref='" + ref + '\'' +
                ", team=" + team +
                ", proxy=" + proxy +
                '}';
    }
}
