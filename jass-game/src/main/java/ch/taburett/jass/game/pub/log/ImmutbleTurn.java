package ch.taburett.jass.game.pub.log;

import ch.taburett.jass.game.api.IPlayerReference;
import ch.taburett.jass.game.spi.ICountModeParametrized;
import ch.taburett.jass.game.spi.IRankModeParametrized;

import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class ImmutbleTurn {
    public final List<ImmutableLogEntry> log;

    public ImmutbleTurn(List<ImmutableLogEntry> log) {
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
    IPlayerReference whoTakes(IRankModeParametrized rankModeParametrized) {
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

    private static class Single<T> implements Collector<T, Single<T>.SingleShit,T> {

        private class SingleShit {
            T entry = null;
            public T getEntry() {
                if(entry != null) {
                    return entry;
                }
                throw new IllegalArgumentException("Chabis");
            }
            public void setEntry(T entry) {
                if(this.entry!= null) {
                    throw new IllegalStateException("Chabis");
                }
                this.entry = entry;
            }

            SingleShit combine( SingleShit other ) {
               if(entry != null && other.entry != null ) {
                   throw new IllegalArgumentException("Chabis");
               }
               return entry != null ? this : other;
            }
        }

        @Override
        public Supplier<SingleShit> supplier() {
            return SingleShit::new;
        }

        @Override
        public BiConsumer<SingleShit, T> accumulator() {
            return SingleShit::setEntry;
        }

        @Override
        public BinaryOperator<SingleShit> combiner() {
            return SingleShit::combine;
        }

        @Override
        public Function<SingleShit, T> finisher() {
            return SingleShit::getEntry;
        }

        @Override
        public Set<Characteristics> characteristics() {
            return Set.of();
        }
    }

}

