package com.asafalima.websocket.services;

import ch.taburett.jass.game.Game;
import ch.taburett.jass.game.spi.messages.IJassMessage;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.asafalima.websocket.services.ProxyGame.GAME_STATE.NEW;
import static com.asafalima.websocket.services.ProxyGame.GAME_STATE.STARTED;

public class ProxyGame {
    public GAME_STATE state;
    private final ConcurrentHashMap<Game.PlayerReference, IProxyUser> userList = new ConcurrentHashMap<>();
    public final LocalDateTime creationDate;
    public final UUID uuid;
    private String owner;
    private Game game;
    private ProxyInstanceableGame gameInfo;
    private Consumer<IJassMessage> sink;

    public ProxyGame(String owner, ProxyInstanceableGame gameInfo, Game game) {
        this(owner, gameInfo, game, LocalDateTime.now(), UUID.randomUUID());
    }

    private ProxyGame(String ownerUsername, ProxyInstanceableGame gameInfo, Game game, LocalDateTime creationDateTime, UUID uuid) {
        // TODO: games like molotov have variable number of participants
        // -> separate gameConfig and Game
        // Move userreferences to gameConf and unite with roundsuppliere
        this.owner = ownerUsername;
        this.game = game;

        this.gameInfo = gameInfo;
        this.state = NEW;
        this.uuid = uuid;
        this.creationDate = creationDateTime;

        addBot("gagi1");
        addBot("gagi2");
        addBot("gagi3");
        join( ownerUsername );
    }
    public String getCaption() {
        return gameInfo.getCaption();
    }

    public boolean join(String userName) {

        if(userList.values().stream()
                .anyMatch( p -> p.getUserName().equals( userName ))) {
            return false;
        }

        Optional<Game.PlayerReference> nextFreeReference = getNextFreePlayer();
        nextFreeReference.ifPresent(
                nf -> addPlayer(new ProxyUser(userName, nf))
        );

        return nextFreeReference.isPresent();
   }

    private void addPlayer(IProxyUser userName) {
        userList.put(userName.getReference(), userName);
        if(userList.size() == gameInfo.maxPlayers())
        {
            state = STARTED;
            userList.values().forEach(ip -> ip.connect());
            game.start();
        }

    }


    private Optional<Game.PlayerReference> getNextFreePlayer() {
        Optional<Game.PlayerReference> nextFreeReference = game.getPlayers().stream()
                .filter(ref -> !userList.containsKey(ref))
                .findAny();
        return nextFreeReference;
    }

    public void addBot(String botName) {
        var ref = getNextFreePlayer();
        ref.ifPresent( r -> addPlayer( new BotProxy(botName, r )));
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
                .map(Game.PlayerReference::getRef)
                .collect(Collectors.toList());
    }

    public Collection<IProxyUser> getUsers() {
        return userList.values();
    }


}
