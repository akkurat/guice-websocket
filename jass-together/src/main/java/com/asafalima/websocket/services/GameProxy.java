package com.asafalima.websocket.services;

import ch.taburett.jass.game.Game;
import ch.taburett.jass.game.IMessageRouter;
import ch.taburett.jass.game.PlayerReference;
import ch.taburett.jass.game.spi.def.ModeDesider;
import ch.taburett.jass.game.spi.messages.IJassMessage;
import com.asafalima.websocket.endpoints.data.ServerMessage;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import static java.util.stream.Collectors.toMap;

public class GameProxy implements IMessageRouter {
    @Inject
    private RoomService rs;
    private ConcurrentHashMap<String, Game> games = new ConcurrentHashMap<>();
    private Consumer<IJassMessage> consumer;
    Map<PlayerReference, ServerMessage.URef> umap;

    public void message(ServerMessage message)
    {

    }

    void createGame(ServerMessage.URef owner)
    {
        ModeDesider md = new ModeDesider();
        Game game = new Game(md);
                game.getPlayers().stream()
                .collect(toMap(u -> u, u -> null));

        game.start( this );

    }

    @Override
    public void register(Consumer<IJassMessage> consumer) {
        this.consumer = consumer;
    }

    @Override
    public void send(IJassMessage message) {




        rs.getUserList().forEach( urfef -> {
            rs.get(urfef.ref).ifPresent(session -> session.getAsyncRemote() .sendObject(message) );
        });

    }


}
