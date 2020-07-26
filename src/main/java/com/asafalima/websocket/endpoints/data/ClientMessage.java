package com.asafalima.websocket.endpoints.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

    public class ClientMessage {
    public final ServerMessage.TYPE type;
    public final String content;
    public final Map<String, String> params;

    public ClientMessage(
            @JsonProperty("type") ServerMessage.TYPE type,
            @JsonProperty("content") String content,
            @JsonProperty("params") Map<String, String> params
    ) {
        this.type = type;
        this.content = content;
        this.params = params;
    }
}
