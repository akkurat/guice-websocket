package com.asafalima.websocket.services;

import ch.taburett.jass.game.PlayerReference;
import ch.taburett.jass.game.spi.messages.IJassMessage;
import ch.taburett.jass.game.spi.messages.Play;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.BiConsumer;

public class ProxyUser {


    public String getUserName() {
        return userName;
    }

    public PlayerReference getReference() {
        return reference;
    }

    private final String userName;
    private final PlayerReference reference;
    private BiConsumer<String, IJassMessage> sink;
    private final Object lock = new Object();
    private LinkedBlockingQueue<IJassMessage> buffer;

    private ProxyUser(String getUserName, PlayerReference getReference)
    {
        this.userName = getUserName;
        this.reference = getReference;
        this.buffer = new LinkedBlockingQueue<>();
        reference.setProxy(this::receiveServerMessage);
    }

    public static ProxyUser createAndConnect(String getUserName, PlayerReference getReference)
    {
        var proxy = new ProxyUser(getUserName, getReference);
        // Cleaner way outside constructor
        getReference.setProxy(proxy::receiveServerMessage);
        return proxy;
    }


    public void receivePlayerMsg(IJassMessage msg) {
        reference.sendToServer(msg);
    }

    /**
     * Use for gamelogic
     * @param msg
     */
    public void receiveServerMessage(IJassMessage msg) {
        synchronized (lock) {
            buffer.add(msg);
            if(sink!= null) {
                sink.accept(userName, msg);
                removeAllButOne();
            }
        }
    }

    public void userConnected(BiConsumer<String, IJassMessage> sink) {
        synchronized (lock) {
            this.sink = sink;
            buffer.forEach(m -> sink.accept(userName, m));
            removeAllButOne();
        }
    }

    private void removeAllButOne() {
        while(buffer.size() > 1) {
            buffer.remove();
        }
    }

    public void userDisconnected() {
        synchronized (lock) {
            sink = null;
        }
    }
}
