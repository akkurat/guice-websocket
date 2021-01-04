module ch.taburett.jass.game {
   requires ch.taburett.jass.cards;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.databind;
    exports ch.taburett.jass.game;
   exports ch.taburett.jass.game.spi;
}