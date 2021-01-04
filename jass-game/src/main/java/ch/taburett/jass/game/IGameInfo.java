package ch.taburett.jass.game;

import java.util.List;
import java.util.Map;

public interface IGameInfo {
    boolean hasEnded(ImmutableRound currentRound);

    boolean hasEnded(List<ImmutableRound> rounds);


    Map<Team, Integer> getPoints(ImmutableRound currentRound);

    List<Map<Team,Integer>> getPoints();
}
