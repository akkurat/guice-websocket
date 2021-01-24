

module ch.taburett.jass.game.api {
    requires transitive ch.taburett.jass.cards;
    requires com.fasterxml.jackson.annotation;
    requires lombok;
    requires com.google.common;
    requires com.fasterxml.jackson.databind;
    exports ch.taburett.jass.game.api;

    exports ch.taburett.jass.game.spi;
    exports ch.taburett.jass.game.spi.events.server;
    exports ch.taburett.jass.game.spi.events.user;
    exports ch.taburett.jass.game.spi.impl;
    exports ch.taburett.jass.game.spi.events;
    exports ch.taburett.jass.game.pub.log;
    exports ch.taburett.jass.game.pub;
}