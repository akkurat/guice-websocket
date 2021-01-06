package ch.taburett.gameworld.services;

import ch.taburett.jass.game.spi.events.user.DecideEvent;
import ch.taburett.jass.game.spi.events.user.Play;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

import java.security.Principal;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.stream.Collectors;

import static ch.taburett.gameworld.services.ProxyGame.GAME_PLAY;

@Service
@Controller
public class GameList {

    public static final String GAME_GAMES = "/game/games";
    public static final String GAME_JOINED = "/game/joined";
    public static final String USER_GAME_PLAY = "/user/game/play/";
    private final ScheduledExecutorService executorService;
    private GameFactory gf;
    private GameStorage gs;
    private SimpMessageSendingOperations simp;
    private final ConcurrentHashMap<String, SubscriptionSink> subscriptions = new ConcurrentHashMap<>();


    public GameList(GameFactory gf,
                    GameStorage gs,
                    SimpMessagingTemplate simp) {
        this.gf = gf;
        this.gs = gs;
        this.simp = simp;
        executorService = Executors.newSingleThreadScheduledExecutor();
    }


    public ProxyGame createGame(String owner, String type) {
        var pg = gf.createGame(owner, type);
        gs.put(pg.uuid, pg);
        simp.convertAndSend(GAME_GAMES, gs.getAllGames());
        return pg;
    }

    public void deleteGame(String id)
    {
        gs.games.remove(id);
        simp.convertAndSend(GAME_GAMES, gs.getAllGames());
    }



    @EventListener
    public void handleSubscribeEvent(SessionSubscribeEvent subscribeEvent) {
        // Pity: Somehow subscription is not necessarily ready
        // this is not a good way to do this
        //
        executorService.execute( () -> {

        StompHeaderAccessor wrap = StompHeaderAccessor.wrap(subscribeEvent.getMessage());
        // TODO: this could be solved by @SubscribeMapping @SendTo
        String destination = wrap.getDestination();
        System.out.println("subscribe" + destination);
        String uname = subscribeEvent.getUser().getName();

        if (GAME_GAMES.equals(destination)) {
            simp.convertAndSend(GAME_GAMES, gs.getAllGames());
        }

        if (("/user" + GAME_JOINED).equals(destination)) {
//            simp.convertAndSendToUser(uname, GAME_JOINED, gs.getAllGames());
        }

        if (destination.startsWith(USER_GAME_PLAY)) {
            if (gs.isGameEndpointAllowed(wrap)) {
                String id = GameStorage.extractSuffix(destination, USER_GAME_PLAY);
                ProxyGame proxyGame = gs.get((id));
                // TODO: remember stomp subscription id

                ProxyUser playerByName = proxyGame.getPlayerByName(uname);
                var sink = new SubscriptionSink(
                        simp, id, wrap.getSubscriptionId(), wrap.getSessionId(), playerByName );
                subscriptions.put( wrap.getSubscriptionId(), sink );
                playerByName.userConnected(sink::sendToUser);
            } else {
                String id = GameStorage.extractSuffix(destination, USER_GAME_PLAY);
                simp.convertAndSendToUser(uname, GAME_PLAY+id, "ERROR");
            }
        }

        });
    }
    private void sendToUser(String name, Object msg)
    {
    }

    @EventListener
    public void handleSubscriptionLostEvent( SessionUnsubscribeEvent sessionUnsubscribeEvent ) {
        StompHeaderAccessor wrap = StompHeaderAccessor.wrap(sessionUnsubscribeEvent.getMessage());
        String subscriptionId = wrap.getSubscriptionId();
        disconnectSubscription(subscriptionId);
        System.out.println( sessionUnsubscribeEvent );

    }

    public void disconnectSubscription(String subscriptionId) {
        SubscriptionSink subscriptionSink = subscriptions.remove(subscriptionId);
        if(subscriptionSink != null )
        {
            subscriptionSink.getPlayerByName().userDisconnected();
        }
    }


    @EventListener
    public void handleSubscriptionLostEvent( SessionDisconnectEvent sessionUnsubscribeEvent ) {
        System.out.println( sessionUnsubscribeEvent );
        var subs = subscriptions.values().stream()
                .filter(s -> s.getSessionId() == sessionUnsubscribeEvent.getSessionId() )
                .map(SubscriptionSink::getSubscriptionId)
                .collect(Collectors.toList());

        subs.forEach( this::disconnectSubscription );
    }



    public void join(String id, Principal user) {
        var pg = gs.get((id));
        pg.join(user.getName());
        simp.convertAndSend(GAME_JOINED, pg);
        simp.convertAndSendToUser(user.getName(), GAME_JOINED, pg);
    }


    @MessageMapping("/cmds/play/{game}")
    public void play(@DestinationVariable String game, @Payload GameCmdPlay play, StompHeaderAccessor headerAccessor) {
        if (gs.isGameEndpointAllowed(headerAccessor)) {
            var pg = gs.get((game));
            ProxyUser playerByName = pg.getPlayerByName(headerAccessor.getUser().getName());
            var playEvent = new Play(play.jassCard());
            playerByName.receivePlayerMsg(playEvent);
        }
    }

    @MessageMapping("/cmds/decide/{game}")
    public void decide(@DestinationVariable String game, @Payload DecideEvent decide, StompHeaderAccessor headerAccessor) {
        if (gs.isGameEndpointAllowed(headerAccessor)) {
            var pg = gs.get((game));
            ProxyUser playerByName = pg.getPlayerByName(headerAccessor.getUser().getName());
            playerByName.receivePlayerMsg(decide);
        }
    }

}
