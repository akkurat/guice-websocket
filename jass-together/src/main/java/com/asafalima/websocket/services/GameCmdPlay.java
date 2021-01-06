package com.asafalima.websocket.services;

import ch.taburett.jass.cards.*;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GameCmdPlay {
    final String code;
    final Card payload;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public GameCmdPlay(@JsonProperty("code") String code, @JsonProperty("payload") Card payload) {
        this.code = code;
        this.payload = payload;
    }
    public JassCard jassCard () {
        return DeckUtil.getInstance().getJassCard(payload.color, payload.value);
    }

    static class Card{
        final JassValue value;
        final JassColor color;
//    @JsonDeserialize()

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public Card(@JsonProperty("value") JassValue value, @JsonProperty("color") JassColor color) {
            this.value = value;
            this.color = color;
        }

    }
}
