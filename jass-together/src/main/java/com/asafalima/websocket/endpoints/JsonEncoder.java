package com.asafalima.websocket.endpoints;

import com.asafalima.websocket.endpoints.data.ServerMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.websocket.EncodeException;
import jakarta.websocket.Encoder;
import jakarta.websocket.EndpointConfig;
import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;

public class JsonEncoder implements Encoder.Text<Serializable> {

    @Override
    public void init(EndpointConfig config) {}

    @Override
    public String encode(Serializable payload) throws EncodeException {
        try {
            return new ObjectMapper().writeValueAsString( payload);
        } catch (JsonProcessingException e) {
            throw new EncodeException(payload, e.getMessage(), e);
        }
    }



    @Override
    public void destroy() {}
}

