package com.asafalima.websocket.endpoints;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GameCmdJoin {
    public final String id;

    GameCmdJoin(@JsonProperty("id") String id)
    {
        this.id = id;
    }
}
