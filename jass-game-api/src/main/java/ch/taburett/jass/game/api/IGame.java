package ch.taburett.jass.game.api;

import java.util.List;

public interface IGame {
    List<IPlayerReference> getPlayers();

    void start();
}
