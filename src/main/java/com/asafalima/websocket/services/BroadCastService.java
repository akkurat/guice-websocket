package com.asafalima.websocket.services;

import com.asafalima.websocket.endpoints.EchoEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.WeakHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@Singleton
public class BroadCastService {
    private static final Logger LOGGER = LoggerFactory.getLogger(BroadCastService.class);

    private final WeakHashMap<Object, Consumer<String>> listeners = new WeakHashMap<>();
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

    public void register( Object whateverYourKeyIs, Consumer<String> listener)
    {
        listeners.put( whateverYourKeyIs, listener);
    }

    public void broadCast( String message )
    {
        listeners.values().forEach(c -> c.accept(message));
    }

    private void execute() {
        String message = msg.getBroadcast();
        LOGGER.info("Broadcasting message: {}, to clients: {}", message, listeners.keySet());
        broadCast( message );
    }



}
