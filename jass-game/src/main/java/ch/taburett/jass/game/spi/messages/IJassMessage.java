package ch.taburett.jass.game.spi.messages;

import ch.taburett.jass.game.Game;
import ch.taburett.jass.game.IPlayerReference;

import java.io.Serializable;

public interface IJassMessage<T> extends Serializable {
    String getCode();

    default IPlayerReference getFrom() {
        return null;
    }

    default IPlayerReference getTo() {
        return null;
    }

    T getPayload();



}
