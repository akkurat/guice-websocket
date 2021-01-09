package ch.taburett.jass.game.pub;

import ch.taburett.jass.game.pub.log.GenericImmutableTrick;

import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class Single<T> implements Collector<T, Single<T>.SingleShit, T> {

    public class SingleShit {
        T entry = null;

        public T getEntry() {
            if (entry != null) {
                return entry;
            }
            throw new IllegalArgumentException("Chabis");
        }

        public void setEntry(T entry) {
            if (this.entry != null) {
                throw new IllegalStateException("Chabis");
            }
            this.entry = entry;
        }

        SingleShit combine(SingleShit other) {
            if (entry != null && other.entry != null) {
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
