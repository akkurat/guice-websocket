package ch.taburett.jass.game;

import ch.taburett.jass.game.spi.messages.IJassMessage;

import java.util.function.Consumer;

public interface IMessageRouter {
    void register(Consumer<IJassMessage> consumer);

    void send(IJassMessage message);
}
