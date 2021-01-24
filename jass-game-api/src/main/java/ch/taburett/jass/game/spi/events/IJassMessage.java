package ch.taburett.jass.game.spi.events;


import java.io.Serializable;

public interface IJassMessage<T> extends Serializable {
    String getCode();

    T getPayload();

}
