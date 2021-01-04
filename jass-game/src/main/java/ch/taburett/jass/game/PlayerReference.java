package ch.taburett.jass.game;

import ch.taburett.jass.game.spi.messages.IJassMessage;
import ch.taburett.jass.game.spi.messages.Play;

import java.util.function.Consumer;

public class PlayerReference implements IPlayerReference {

    private Game game;

    @Override
    public String getRef() {
        return ref;
    }

    private final String ref;
    private Team team;
    private Consumer<IJassMessage> proxy;

    PlayerReference(Game game, String ref, Team team) {
        this.ref = ref;
        this.team = team;
        this.game = game;
    }

    public void setProxy(Consumer<IJassMessage> proxy) {
        this.proxy = proxy;
    }

    public void sendToUser(IJassMessage event) {
        if (proxy == null) {
            throw new IllegalStateException("Proxy must always be present");
        }
        proxy.accept(event);
    }

    public void sendToServer(IJassMessage event) {
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
