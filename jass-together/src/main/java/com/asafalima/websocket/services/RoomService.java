package com.asafalima.websocket.services;

import com.asafalima.websocket.endpoints.data.ServerMessage;

import jakarta.inject.Singleton;
import jakarta.websocket.Session;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.WeakHashMap;
import java.util.stream.Collectors;

public class RoomService {

    private final WeakHashMap<Session,String> userNames = new WeakHashMap<>();

    public void registerUser( Session session, String name )
    {
        userNames.put(session, name);
    }


    public String getUser( Session session )
    {
       return userNames.get( session );
    }

    public ServerMessage.URef getUserRef(Session session )
    {
        String caption = getUser(session);
        if( caption != null )
        {
            return new ServerMessage.URef(session.getId(), caption);
        }
        return null;
    }


    public String get(Session session) {
        return getUser( session );
    }

    public Optional<Session> get(String sessionId) {
        return userNames.keySet().stream()
                .filter( s -> s.getId().equals(sessionId))
                .findAny();
    }
    public List<ServerMessage.URef> getUserList() {
        return getUserList(true);
    }

    public List<ServerMessage.URef> getUserList(boolean onlyOpen) {
        return userNames.entrySet().stream()
                .filter( e -> e.getKey().isOpen() )
                .map(this::format)
                .collect(Collectors.toList());
    }

    private ServerMessage.URef format(Map.Entry<Session, String> sessionStringEntry) {
        var s = sessionStringEntry.getKey();
        var n = sessionStringEntry.getValue();
        return new ServerMessage.URef( s.getId(), n);
    }
}
