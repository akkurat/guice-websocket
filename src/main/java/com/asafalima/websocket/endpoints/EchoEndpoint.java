package com.asafalima.websocket.endpoints;

import com.asafalima.websocket.services.BroadCastService;
import com.asafalima.websocket.services.MessagingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.websocket.CloseReason;
import javax.websocket.CloseReason.CloseCodes;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Singleton
@ServerEndpoint(value = "/echo")
public class EchoEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(EchoEndpoint.class);

    private final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(5);
    private final List<Session> clientsSessions = new ArrayList<>();
    private final MessagingService messagingService;
    private final WeakHashMap<Session,String> userNames = new WeakHashMap<>();

    @Inject
    public EchoEndpoint(final MessagingService messagingService) {
        this.messagingService = messagingService;
    }

    @Inject
    private BroadCastService broadCastService;

    @PostConstruct
    public void init() {

        scheduledExecutorService.scheduleAtFixedRate(() -> clientsSessions.forEach(session -> {
            try {
                if (!session.isOpen()) {
                    clientsSessions.remove(session);
                    return;
                }

                String message = messagingService.getMessage(userNames.get(session));
                LOGGER.info("Sending message: {}, to client: {}", message, session);
                session.getBasicRemote().sendText(message);
            } catch (Exception e) {
                LOGGER.error("Error while sending message to client: {}", session, e);
            }
        }), 0, 5, TimeUnit.SECONDS);
    }

    @PreDestroy
    public void shutdown() {
        scheduledExecutorService.shutdown();
        clientsSessions.forEach(session -> {
            try {
                if (!session.isOpen()) {
                    return;
                }

                session.close(new CloseReason(CloseCodes.SERVICE_RESTART, "Shutting down"));
            } catch (Exception e) {
                LOGGER.error("Error while closing session", e);
            }
        });
    }

    @OnOpen
    public void onWebSocketConnect(Session session) {
        LOGGER.info("Socket Connected, session: {}", session);
        clientsSessions.add(session);
        broadCastService.register( session, msg ->
        { if(session.isOpen()) {
            try {
                session.getBasicRemote().sendText(msg);
            } catch (IOException e) {
                LOGGER.error("shit", e);
            }
        }
        } );
    }

    @OnMessage
    public void onMessage(Session session, String message) throws IOException {

        // User is registered
        if( userNames.containsKey(session)) {
            if(message.startsWith(">"))
            {
                String payload = message.substring(1);
                session.getBasicRemote().sendText(synchronCmd(session,payload));
                payload.split(" ");
            }
            broadCastService.broadCast(userNames.get(session) + " says: " + message);
        }
        else
        {
            // Assuming first message is the username
            userNames.put(session, message);
            session.getBasicRemote().sendText("Welcome " + message);
        }

    }

    private String synchronCmd(Session session, String payload) {
        String[] parts = payload.split(" ");
        if( parts.length == 0 )
            return "Empty command...";
        switch (parts[0]) {
            case "mv":
                if( parts.length>1) {
                    String name = parts[1];
                    userNames.put(session, name);
                    return "New username: "+name;
                }
                break;
            case "ls":
                return "Logged in users: " + userNames.values();
        }

        return null;

    }

    @OnClose
    public void onWebSocketClose(Session session, CloseReason reason) {
        LOGGER.info("Socket Closed, session: {}, reason: {}", session, reason);
        clientsSessions.remove(session);
    }

    @OnError
    public void onWebSocketError(Session session, Throwable throwable) {
        LOGGER.error("An error occurred while communicating with the client, session: {}", session, throwable);
        clientsSessions.remove(session);
    }

}
