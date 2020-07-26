package com.asafalima.websocket.endpoints;

import com.asafalima.websocket.endpoints.data.ServerMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;
import java.io.IOException;
import java.io.Writer;

public class JsonEncoder implements Encoder.Text<ServerMessage> {

    @Override
    public void init(EndpointConfig config) {}

    @Override
    public String encode(ServerMessage payload) throws EncodeException {
        try {
            return new ObjectMapper().writeValueAsString( payload);
        } catch (JsonProcessingException e) {
            throw new EncodeException(payload, e.getMessage(), e);
        }
    }



    @Override
    public void destroy() {}
}

