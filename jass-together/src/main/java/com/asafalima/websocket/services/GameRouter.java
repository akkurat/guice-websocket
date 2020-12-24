package com.asafalima.websocket.services;

import com.asafalima.websocket.endpoints.data.ServerMessage;

import jakarta.inject.Singleton;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@Singleton
public class GameRouter {
    Set<GameProxy> games = new CopyOnWriteArraySet<>();
    public void message(ServerMessage sMessage) {
        if( "NEW_GAME".equals(sMessage.content) )
        {
            games.add( new GameProxy() );
        }
    }

    public List<String> rooms() {
        return List.of("Gagi", "Hansi");
    }
}
