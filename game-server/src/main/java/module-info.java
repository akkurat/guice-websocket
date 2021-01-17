open module guice.websocket.jass.together.main {
//    opens ch.taburett.gameworld to spring.core, spring.beans, spring.context;
//    opens ch.taburett.gameworld.endpoints to spring.core, spring.beans, spring.context;
//    opens ch.taburett.gameworld.wiring to spring.core, spring.beans, spring.context;
//    opens ch.taburett.gameworld.services to spring.core, spring.beans, spring.context;
//    opens ch.taburett.gameworld;
//    opens ch.taburett.gameworld.endpoints;
//    opens ch.taburett.gameworld.wiring;
//    opens ch.taburett.gameworld.services;
    requires com.fasterxml.jackson.databind;
    requires ch.taburett.jass.game;
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires org.slf4j;
    requires spring.websocket;
    requires spring.context;
    requires spring.messaging;
    requires spring.security.config;
    requires spring.web;
    requires spring.beans;
    requires spring.security.core;
    requires spring.security.web;
    requires static lombok;
    requires crap;
}