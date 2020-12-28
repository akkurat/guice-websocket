package ch.taburett.jass.game.spi.messages;

import ch.taburett.jass.game.Game;

import java.io.Serializable;

public interface IJassMessage<T> extends Serializable {
    String getCode();

    default Game.PlayerReference getFrom() {
        return null;
    }

    default Game.PlayerReference getTo() {
        return null;
    }

    T getPayload();



}
