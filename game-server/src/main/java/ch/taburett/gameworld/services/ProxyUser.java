package ch.taburett.gameworld.services;

import ch.taburett.jass.game.api.IPlayerReference;
import ch.taburett.jass.game.spi.events.server.IServerMessage;
import ch.taburett.jass.game.spi.events.user.IUserEvent;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.BiConsumer;

public class ProxyUser {

    Logger logger = LoggerFactory.getLogger(ProxyUser.class);

    public String getUserName() {
        return userName;
    }

    public IPlayerReference getReference() {
        return reference;
    }

    private final String userName;
    private final IPlayerReference reference;
    private BiConsumer<String, IServerMessage<?>> sink;
    private final Object lock = new Object();
    private Map<Integer,LinkedBlockingQueue<IServerMessage<?>>> buffer;

    private ProxyUser(String getUserName, IPlayerReference getReference)
    {
        this.userName = getUserName;
        this.reference = getReference;
        this.buffer = new HashMap<>();
        reference.setProxy(this::receiveServerMessage);
    }

    public static ProxyUser createAndConnect(String getUserName, IPlayerReference getReference)
    {
        var proxy = new ProxyUser(getUserName, getReference);
        // Cleaner way outside constructor
        getReference.setProxy(proxy::receiveServerMessage);
        return proxy;
    }


    public void receivePlayerMsg(IUserEvent<?> msg) {
        reference.sendToServer(msg);
    }

    /**
     * Use for gamelogic
     * @param msg
     */
    public void receiveServerMessage(IServerMessage<?> msg) {
        synchronized (lock) {
            int type = msg.getBuffertype();
            buffer.computeIfAbsent( type, k -> new LinkedBlockingQueue<>() );
            buffer.get(type).add(msg);
            if(sink!= null) {
                sink.accept(userName, msg);
                removeAllButOne();
            }
        }
    }

    public void userConnected(BiConsumer<String, IServerMessage<?>> sink) {
        synchronized (lock) {
            this.sink = sink;
            for (LinkedBlockingQueue<IServerMessage<?>> b : buffer.values()) {
                for (IServerMessage<?> m : b) {
                    sink.accept(userName, m);
                }
            }
            removeAllButOne();
        }
    }

    private void removeAllButOne() {
        for(val v : buffer.values() ) {
            while (v.size() > 1) {
                v.remove();
            }
        }
    }

    public void userDisconnected() {
        synchronized (lock) {
            sink = null;
        }
    }
}
