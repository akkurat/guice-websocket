package com.asafalima.websocket.services;

import com.asafalima.websocket.endpoints.EchoEndpoint;
import com.asafalima.websocket.endpoints.data.ServerMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static com.asafalima.websocket.endpoints.data.ServerMessage.TYPE.BC;

@Singleton
public class BroadCastService {
    private static final Logger LOGGER = LoggerFactory.getLogger(BroadCastService.class);

    private final WeakHashMap<Object, Consumer<ServerMessage>> listeners = new WeakHashMap<Object, Consumer<ServerMessage>>();
    private final MessagingService msg;


    @Inject
    public BroadCastService(MessagingService msg)
    {
        this.msg = msg;
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(this::execute,
                10, 10, TimeUnit.SECONDS
        );
    }

    public void register( Object whateverYourKeyIs, Consumer<ServerMessage> listener)
    {
        listeners.put( whateverYourKeyIs, listener);
    }

    public void broadCast( ServerMessage message )
    {
        broadCast( message, Collections.emptySet() );
    }
    public void broadCast( ServerMessage message, Set<Object> filterKeys )
    {
        listeners.entrySet().stream()
                .filter( e -> !filterKeys.contains(e.getKey()) )
                .forEach(e -> e.getValue().accept(message));
    }

    private void execute() {
        String message = msg.getBroadcast();
        LOGGER.info("Broadcasting message: {}, to clients: {}", message, listeners.keySet());
        broadCast( new ServerMessage(BC,message, EchoEndpoint.LADIALAD));
    }



}
