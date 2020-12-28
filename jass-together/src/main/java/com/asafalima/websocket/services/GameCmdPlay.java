package com.asafalima.websocket.services;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GameCmdPlay {
//    @JsonDeserialize()
    public final String card;

    public GameCmdPlay(@JsonProperty("card")String card) {
        this.card = card;
    }
}
