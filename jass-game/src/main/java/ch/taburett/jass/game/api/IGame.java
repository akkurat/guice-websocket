package ch.taburett.jass.game.api;

import ch.taburett.jass.game.impl.PlayerReference;

import java.util.Collection;
import java.util.List;

public interface IGame {
    List<IPlayerReference> getPlayers();

    void start();
}
