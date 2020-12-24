package com.asafalima.websocket.di;

import com.google.inject.Injector;
import jakarta.websocket.server.ServerEndpointConfig;


/**
 * @author Asaf Alima
 */
public class GuiceConfigurator extends ServerEndpointConfig.Configurator {

    private final Injector injector;

    public GuiceConfigurator(Injector injector) {
        this.injector = injector;
    }

    public <T> T getEndpointInstance(Class<T> endpointClass) throws InstantiationException {
        return injector.getInstance(endpointClass);
    }

}
