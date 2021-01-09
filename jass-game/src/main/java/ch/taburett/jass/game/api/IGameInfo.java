package ch.taburett.jass.game.api;

import ch.taburett.jass.game.impl.internal.Team;
import ch.taburett.jass.game.pub.log.ImmutableRound;

import java.util.List;
import java.util.Map;

public interface IGameInfo {
    boolean hasEnded(ImmutableRound currentRound);

    boolean hasEnded(List<ImmutableRound> rounds);


    Map<? extends ITeam, Integer> getPoints(ImmutableRound currentRound);

    List<? extends Map<? extends ITeam, ? extends Integer>> getPoints();

    List<ImmutableRound> getLog();
}
