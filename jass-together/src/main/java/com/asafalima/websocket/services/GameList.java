package com.asafalima.websocket.services;

import ch.taburett.jass.game.PlayerReference;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class GameList {

    private GameFactory gf;

    public GameList(GameFactory gf) {
        this.gf = gf;
    }

    CopyOnWriteArrayList<ProxyGame> list = new CopyOnWriteArrayList<>();

    public List<ProxyGame> getAllGames() {
        return list;
    }

    public ProxyGame createGame( ProxyUser owner, String type ) {
        var pg = gf.createGame(owner, type);
        list.add(pg);

        return pg;
    }

}
