module ch.taburett.jass.game {
    requires ch.taburett.jass.cards;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.databind;

    exports ch.taburett.jass.game.api;
    exports ch.taburett.jass.game.pub.log;

    exports ch.taburett.jass.game.spi;
    exports ch.taburett.jass.game.spi.events.server;
    exports ch.taburett.jass.game.spi.events.user;
}