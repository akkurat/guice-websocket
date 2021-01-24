package ch.taburett.gameworld.services;

import ch.taburett.gameserver.spi.IGameProvider;
import ch.taburett.gameserver.spi.IProxyInstanceableGame;
import ch.taburett.jass.game.impl.GGameFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class GameFactory {


    public final static String GAME_TYPES = "/game/gametypes";
    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    private final Map<String, IProxyInstanceableGame> map;

    ServiceLoader<IGameProvider> loader = ServiceLoader.load(IGameProvider.class);

    private SimpMessagingTemplate simp;

    GameFactory(SimpMessagingTemplate simp) {
        this.simp = simp;

        Map<String,IProxyInstanceableGame> map = new HashMap<>();
        for( var l: loader ) {
           map.putAll(l.getGames());
        }
        this.map = Map.copyOf(map);

    }

    public Map<String, IProxyInstanceableGame> getPossibleGames() {
        return map;
    }

    public ProxyGame createGame( String owner, String type ) {
        IProxyInstanceableGame proxyInstanceableGame = map.get(type);
        var roundSupplier = proxyInstanceableGame.create(owner);
        var game = GGameFactory.create(roundSupplier);
        return new ProxyGame(owner,proxyInstanceableGame,game, simp);
    }



    @EventListener
    public void handleSubscribeEvent(SessionSubscribeEvent subscribeEvent)
    {
        // Very poor man solution
        executorService.schedule( () -> {
            StompHeaderAccessor wrap = StompHeaderAccessor.wrap(subscribeEvent.getMessage());
            if (GAME_TYPES.equals(wrap.getDestination())) {
                simp.convertAndSend(GAME_TYPES, map);
            }
        }, 1, TimeUnit.MILLISECONDS);
    }

}
