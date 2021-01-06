package ch.taburett.gameworld.services;

import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class GameStorage {
    ConcurrentHashMap<String, ProxyGame> games = new ConcurrentHashMap<>();
    public List<ProxyGame> getAllGames() {
        return games.values().stream()
                .sorted( Comparator.comparing(pg -> pg.creationDate))
                .collect(Collectors.toList());
    }

    public ProxyGame get(String uuid) {
        return games.get(uuid);
    }
    public void put(String uuid, ProxyGame pg) {
        games.put(uuid, pg);
    }
    public boolean isGameEndpointAllowed(StompHeaderAccessor wrap) {
        boolean gameEndpointAllowed = true;

        String destination = wrap.getDestination();
        String gameid = extractSuffix(destination, GameList.USER_GAME_PLAY);
        if(gameid != null) {
            gameEndpointAllowed = false;

            var pg = games.get((gameid));
            if(pg != null )
            {
                String name = wrap.getUser().getName();
                if(pg.hasPlayer(name) ) {
                    gameEndpointAllowed = true;
                }
            }
        }
        return gameEndpointAllowed;
    }

    public static String extractSuffix(String string, String prefix) {
        String gameid = null;
        if(string.startsWith(prefix)) {
            gameid = string.substring(prefix.length());
        }
        return gameid;
    }
}
