package ch.taburett.gamemodi.std;

import ch.taburett.gameserver.spi.IProxyInstanceableGame;
import ch.taburett.jass.game.spi.IRoundSupplier;
import ch.taburett.jass.game.spi.impl.ModeDesider;

public class AllAllowedJassGameI implements IProxyInstanceableGame {
    @Override
    public int minPlayers() {
        return 4;
    }

    @Override
    public String getCaption() {
        return "Alles Erlaubt Schieber, Mehrfach Zählung";
    }

    @Override
    public IRoundSupplier create(String owner) {
        return new ModeDesider();
    }

}
