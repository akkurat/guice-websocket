package com.asafalima.websocket.services;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.security.Principal;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class GameList {

    public static final String GAME_GAMES = "/game/games";
    public static final String GAME_JOINED = "/game/joined";
    private GameFactory gf;
    private SimpMessageSendingOperations simp;
    ConcurrentHashMap<UUID, ProxyGame> map = new ConcurrentHashMap<>();

    public GameList(GameFactory gf, SimpMessagingTemplate simp) {
        this.gf = gf;
        this.simp = simp;
    }


    public List<ProxyGame> getAllGames() {
        return map.values().stream()
                .sorted( Comparator.comparing( pg -> pg.creationDate))
                .collect(Collectors.toList());
    }

    public ProxyGame createGame(String owner, String type ) {
        var pg = gf.createGame(owner, type);
        map.put(pg.uuid, pg);

        simp.convertAndSend(GAME_GAMES, getAllGames());
        return pg;
    }

    @EventListener
    public void handleSubscribeEvent(SessionSubscribeEvent subscribeEvent)
    {
        StompHeaderAccessor wrap = StompHeaderAccessor.wrap(subscribeEvent.getMessage());
        if(GAME_GAMES.equals(wrap.getDestination())) {
            simp.convertAndSend(GAME_GAMES, getAllGames());
        }
    }

    public void join(String id, Principal user) {
        var pg = map.get(UUID.fromString(id));
        pg.join( user.getName() );
        simp.convertAndSend( GAME_JOINED, pg );
        simp.convertAndSendToUser( user.getName(), GAME_JOINED, pg );
    }
}
