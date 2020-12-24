package ch.taburett.jass.game.spi.messages;

import ch.taburett.jass.game.PlayerReference;

import java.io.Serializable;

public interface IJassMessage<T> extends Serializable {
    String getCode();

    PlayerReference getFrom();

    PlayerReference getTo();

    T getPayload();
}
