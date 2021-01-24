import ch.taburett.jass.game.spi.IJassGameProvider;

module ch.taburett.jass.game {
    requires static lombok;
    requires transitive ch.taburett.jass.cards;
    exports ch.taburett.jass.game.impl to com.fasterxml.jackson.databind;
    exports ch.taburett.jass.game.impl.internal to com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.databind;
    requires com.google.common;
    requires org.slf4j;
    requires ch.taburett.jass.game.api;

    uses IJassGameProvider;
}