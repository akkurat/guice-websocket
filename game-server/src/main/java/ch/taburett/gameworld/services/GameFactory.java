package ch.taburett.gameworld.services;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class GameFactory {


    public final static String GAME_TYPES = "/game/gametypes";
    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    Map<String, ProxyInstanceableGame> list = Map.of(
            "all", new AllAllowedJassGame(),
            "boring", new BoringAllowedJassGame()
            );
    private SimpMessagingTemplate simp;

    GameFactory(SimpMessagingTemplate simp) {
        this.simp = simp;
    }

    public Map<String, ProxyInstanceableGame> getPossibleGames() {
        return list;
    }

    public ProxyGame createGame( String owner, String type ) {
        return list.get(type).create(owner,simp);
    }



    @EventListener
    public void handleSubscribeEvent(SessionSubscribeEvent subscribeEvent)
    {
        // Very poor man solution
        executorService.schedule( () -> {
            StompHeaderAccessor wrap = StompHeaderAccessor.wrap(subscribeEvent.getMessage());
            if (GAME_TYPES.equals(wrap.getDestination())) {
                simp.convertAndSend(GAME_TYPES, list);
            }
        }, 1, TimeUnit.MILLISECONDS);
    }

}
