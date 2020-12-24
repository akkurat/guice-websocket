package com.asafalima.websocket.di;

import com.asafalima.websocket.endpoints.JsonDecoder;
import com.asafalima.websocket.endpoints.JsonEncoder;
import com.asafalima.websocket.services.BroadCastService;
import com.asafalima.websocket.services.MessagingService;
import com.asafalima.websocket.services.RoomService;
import com.google.inject.AbstractModule;
import com.asafalima.websocket.endpoints.EchoEndpoint;

/**
 * @author Asaf Alima
 */
public class WebsocketModule extends AbstractModule {

    @Override
    protected void configure() {
//        binder().requireExplicitBindings();
//        bind(MessagingService.class);
//        bind(BroadCastService.class);
//        bind(EchoEndpoint.class);
//        bind(RoomService.class);
    }

}
