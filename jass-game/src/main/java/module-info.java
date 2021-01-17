module ch.taburett.jass.game {
    requires static lombok;
    requires transitive ch.taburett.jass.cards;
    exports ch.taburett.jass.game.impl to com.fasterxml.jackson.databind;
    exports ch.taburett.jass.game.impl.internal to com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.databind;
    requires com.google.common;
    requires org.slf4j;
    exports ch.taburett.jass.game.api;
    exports ch.taburett.jass.game.pub.log;
    exports ch.taburett.jass.game.pub;

    exports ch.taburett.jass.game.spi;
    exports ch.taburett.jass.game.spi.events.server;
    exports ch.taburett.jass.game.spi.events.user;
    exports ch.taburett.jass.game.spi.impl;
    exports ch.taburett.jass.game.spi.events;
}