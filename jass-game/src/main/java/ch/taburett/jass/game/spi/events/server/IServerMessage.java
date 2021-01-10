package ch.taburett.jass.game.spi.events.server;

import ch.taburett.jass.game.api.IPlayerReference;
import ch.taburett.jass.game.impl.PlayerReference;
import ch.taburett.jass.game.spi.events.IJassMessage;

public interface IServerMessage<T> extends IJassMessage<T> {
    public IPlayerReference getTo();
}
