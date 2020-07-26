package com.asafalima.websocket.endpoints;

import com.asafalima.websocket.endpoints.data.ClientMessage;
import com.asafalima.websocket.endpoints.data   .ServerMessage;
import com.asafalima.websocket.services.BroadCastService;
import com.asafalima.websocket.services.MessagingService;
import com.asafalima.websocket.services.RoomService;
import com.google.common.collect.ImmutableSet;
import org.eclipse.jetty.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.websocket.*;
import javax.websocket.CloseReason.CloseCodes;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.asafalima.websocket.endpoints.data.ServerMessage.TYPE.*;

@Singleton
@ServerEndpoint(value = "/echo", encoders = {JsonEncoder.class}, decoders = {JsonDecoder.class})
public class EchoEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(EchoEndpoint.class);
    public static final ServerMessage.URef LADIALAD = new ServerMessage.URef("server", "Server");

    private final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(5);
    private final List<Session> clientsSessions = new ArrayList<>();
    private final MessagingService messagingService;


    @Inject
    public EchoEndpoint(final MessagingService messagingService) {
        this.messagingService = messagingService;
    }

    @Inject
    private BroadCastService broadCastService;

    @Inject
    private RoomService us;

    @PostConstruct
    public void init() {

        scheduledExecutorService.scheduleAtFixedRate(() -> clientsSessions.forEach(session -> {
            try {
                if (!session.isOpen()) {
                    clientsSessions.remove(session);
                    return;
                }

                String message = messagingService.getMessage(us.getUser(session));
                LOGGER.info("Sending message: {}, to client: {}", message, session);
                session.getAsyncRemote().sendObject(ServerMessage.dm(LADIALAD,message));
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
        WeakReference<Session> weakSession = new WeakReference<>(session);
        broadCastService.register( session, msg ->
        {
            var s = weakSession.get();
            if(s != null)
            if(s.isOpen()) {
                s.getAsyncRemote().sendObject(msg);
            }
        } );
    }

    @OnMessage
    public void onMessage( ClientMessage cMessage, Session session   ) throws IOException, EncodeException {

        // User is registered
        ServerMessage.URef userRef = us.getUserRef(session);
        if( userRef != null ) {
            if(cMessage.type == INFO)
            {
                session.getBasicRemote().sendObject(synchronCmd(session,cMessage));
            }
            else if(cMessage.type == DM)
            {
                var sessionId = cMessage.params.get("uref");
                var targetSession = us.get( sessionId );
                targetSession.ifPresentOrElse( s -> s.getAsyncRemote().sendObject(
                        ServerMessage.dm(userRef, cMessage.content )),
                            () -> LOGGER.warn(sessionId + " not found...")
                );
            }
            else if(cMessage.type == BC)
            {
                ServerMessage sMessage = ServerMessage.bc(userRef, cMessage.content);
                broadCastService.broadCast(sMessage);
            }
        }
        else
        {
            // Assuming first message is the username
            var username = cMessage.params.get("username");
            if(StringUtil.isBlank(username))
            {
                session.close(new CloseReason(CloseCodes.PROTOCOL_ERROR, "Username cannot be empty."));
                return;
            }
            us.registerUser( session, username );
            session.getAsyncRemote().sendObject(ServerMessage.bc(LADIALAD,"Welcome " + username));
            ServerMessage sMessage = ServerMessage.bc(LADIALAD, username + " entered da room.");
            broadCastService.broadCast(sMessage, ImmutableSet.of(session));
            broadCastService.broadCast(ServerMessage.info(us.getUserList()));
        }

    }

    private ServerMessage synchronCmd(Session session, ClientMessage payload) {

        switch (payload.params.get("cmd")) {
            case "mv":
                if( payload.params.containsKey("username")) {
                    String name = payload.params.get("username");
                    us.registerUser(session, name);
                    return ServerMessage.info("New username: "+name);
                }
                break;
            case "ls":
                return ServerMessage.info(us.getUserList());
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
