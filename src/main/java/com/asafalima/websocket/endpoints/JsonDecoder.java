package com.asafalima.websocket.endpoints;

import com.asafalima.websocket.endpoints.data.ClientMessage;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;
import java.io.IOException;
import java.io.Reader;

public class JsonDecoder implements Decoder.TextStream<ClientMessage>
{

    @Override
    public void init(EndpointConfig config) {
    }


    @Override
    public ClientMessage decode(Reader reader) throws DecodeException, IOException {
        return new ObjectMapper().readValue(reader, ClientMessage.class);
    }




    @Override
    public void destroy() {}
}
