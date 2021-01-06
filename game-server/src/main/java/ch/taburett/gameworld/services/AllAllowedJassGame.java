package ch.taburett.gameworld.services;

import ch.taburett.jass.game.api.IGame;
import ch.taburett.jass.game.pub.GameFactory;
import ch.taburett.jass.game.spi.impl.ModeDesider;
import org.springframework.messaging.simp.SimpMessagingTemplate;

public class AllAllowedJassGame implements ProxyInstanceableGame {
    @Override
    public int minPlayers() {
        return 4;
    }

    @Override
    public String getCaption() {
        return "Alles Erlaubt Schieber, Mehrfach Zählung";
    }

    @Override
    public ProxyGame create(String owner, SimpMessagingTemplate simp) {
        IGame gamge = GameFactory.create(new ModeDesider());
        return new ProxyGame(owner, this, gamge, simp);
    }

}
