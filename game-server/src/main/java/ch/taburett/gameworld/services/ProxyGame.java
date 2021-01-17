package ch.taburett.gameworld.services;

import ch.taburett.gameworld.messages.UserMapEvent;
import ch.taburett.jass.game.api.IGame;
import ch.taburett.jass.game.api.IPlayerReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static ch.taburett.gameworld.services.ProxyGame.GAME_STATE.NEW;
import static ch.taburett.gameworld.services.ProxyGame.GAME_STATE.STARTED;

public class ProxyGame {
    Logger logger = LoggerFactory.getLogger(ProxyGame.class);

    public static final String GAME_PLAY = "/game/play/";
    public GAME_STATE state;
    private final ConcurrentHashMap<IPlayerReference, ProxyUser> userList = new ConcurrentHashMap<IPlayerReference, ProxyUser>();
    public final LocalDateTime creationDate;
    public final String uuid;
    private String owner;
    private IGame game;
    private ProxyInstanceableGame gameInfo;
    private SimpMessagingTemplate simp;

    private final ExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    public ProxyGame(String owner, ProxyInstanceableGame gameInfo, IGame game, SimpMessagingTemplate simp) {
        this(owner, gameInfo, game, LocalDateTime.now(), String.valueOf(ThreadLocalRandom.current().nextInt(1000)), simp);
    }

    private ProxyGame(String ownerUsername, ProxyInstanceableGame gameInfo, IGame game, LocalDateTime creationDateTime, String uuid, SimpMessagingTemplate simp) {
        logger.trace("A TRACE Message");
        logger.debug("A DEBUG Message");
        logger.info("An INFO Message");
        logger.warn("A WARN Message");
        logger.error("An ERROR Message");
        // TODO: games like molotov have variable number of participants
        // -> separate gameConfig and Game
        // Move userreferences to gameConf and unite with roundsuppliere
        this.owner = ownerUsername;
        this.game = game;

        this.gameInfo = gameInfo;
        this.simp = simp;
        // TODO: Move State to Game
        this.state = NEW;
        this.uuid = uuid;
        this.creationDate = creationDateTime;


        join( ownerUsername );
        addBot("gagi1");
        addBot("gagi2");
        addBot("gagi3");
    }
    public String getCaption() {
        return gameInfo.getCaption();
    }

    public boolean join(String userName) {

        if(userList.values().stream()
                .anyMatch( p -> p.getUserName().equals( userName ))) {
            return false;
        }

        Optional<IPlayerReference> nextFreeReference = getNextFreePlayer();
        nextFreeReference.ifPresent(
                nf -> {
                    ProxyUser user = ProxyUser.createAndConnect(userName, nf);
                    addPlayer(user);
                }
        );

        return nextFreeReference.isPresent();
   }

    private void sendUserList() {

        var playerList = game.getPlayers();

        var map = userList.entrySet().stream()
                .collect(Collectors.toUnmodifiableMap(
                       e -> e.getKey().getRef(),
                        e-> e.getValue().getUserName()
                ));

        userList.values()
                .forEach(proxyUser -> {
                    var m = new UserMapEvent(proxyUser.getReference(),
                            new UserMapEvent.UserPayload(playerList, map, proxyUser.getReference().getRef() )
                    );
                    proxyUser.receiveServerMessage(m);
                });
    }


    private void addPlayer(ProxyUser user) {
        userList.put(user.getReference(), user);
        sendUserList();
        if(userList.size() == gameInfo.maxPlayers())
        {
            executor.execute(() -> {
                state = STARTED;
                game.start();
            });
        }

    }


    private Optional<IPlayerReference> getNextFreePlayer() {
        Optional<IPlayerReference> nextFreeReference = game.getPlayers().stream()
                .filter(ref -> !userList.containsKey(ref))
                .findAny();
        return nextFreeReference;
    }

    public void addBot(String botName) {
        var ref = getNextFreePlayer();
        ref.ifPresent( r -> {
            ProxyUser proxyUser = ProxyUser.createAndConnect(r.toString() + "bot_"+botName, r);
            BotProxy botProxy = new BotProxy(proxyUser::receivePlayerMsg);
            proxyUser.userConnected(botProxy::receiveServerMessage);
            addPlayer(proxyUser);
        });
    }

    public boolean hasPlayer(String name) {
        return userList.values().stream()
                .anyMatch( p -> name.equals(p.getUserName()));
    }
    public ProxyUser getPlayerByName(String name) {
        return userList.values().stream()
                .filter( p -> name.equals( p.getUserName() ))
                .findAny()
                .orElse( null );
    }

    // TODO: Approach with Game communicating with Players Directly is better

    public enum GAME_STATE {
        NEW,
        STARTED,
        INTERRUPTED,
        FINISHED
    }

    public List<String> getPlayers() {
        return game.getPlayers().stream()
                .map(IPlayerReference::getRef)
                .collect(Collectors.toList());
    }

    public Collection<ProxyUser> getUsers() {
        return userList.values();
    }


}
