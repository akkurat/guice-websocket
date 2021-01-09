package ch.taburett.jass.game.pub.log;

import ch.taburett.jass.game.api.IPlayerReference;
import ch.taburett.jass.game.pub.Single;
import ch.taburett.jass.game.spi.ICountModeParametrized;
import ch.taburett.jass.game.spi.IRankModeParametrized;

import java.util.List;
import java.util.stream.Collectors;

public class GenericImmutableTrick {
    public final List<ImmutableLogEntry> log;

    public GenericImmutableTrick(List<ImmutableLogEntry> log) {
        this.log = List.copyOf(log);
    }




    /**
     * In Jass the Stich belongs always to the team.
     * However, here we return the individual player
     * and group by team later to be generic
     * I.E. in Tichu this is necessary
     *
     * @param rankModeParametrized
     * @return
     */
    public IPlayerReference whoTakes(IRankModeParametrized rankModeParametrized) {
        var cards = log.stream()
                .map(l-> l.card)
                .collect(Collectors.toList());

        var maxCard = rankModeParametrized.getMaxCard(cards);

        return log.stream()
                .filter(l->l.card == maxCard )
                .map(l -> l.playerReference)
                .collect(new Single<>());
//                .findFirst()
//                .map(l->l.playerReference)
//                .orElseThrow();
    }



    public int sum(ICountModeParametrized mode) {
        return log.stream()
                .mapToInt(l -> mode.getCount(l.card))
                .sum();
    }

}

