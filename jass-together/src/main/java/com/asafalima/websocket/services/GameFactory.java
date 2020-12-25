package com.asafalima.websocket.services;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class GameFactory {


    Map<String, ProxyInstanceableGame> list = Map.of(
            "all", new AllAllowedJassGame(),
            "boring", new BoringAllowedJassGame()
            );

    public Map<String, ProxyInstanceableGame> getPossibleGames() {
        return list;
    }

    public ProxyGame createGame( ProxyUser owner, String type ) {
        return list.get(type).create(owner);
    }

}
