package ch.taburett.gamemodi.std;

import ch.taburett.gameserver.spi.IProxyInstanceableGame;
import ch.taburett.jass.game.spi.IRoundSupplier;
import ch.taburett.jass.game.spi.impl.BoringRoundSupplier;

public class BoringAllowedJassGameI implements IProxyInstanceableGame {
    @Override
    public int minPlayers() {
        return 4;
    }

    @Override
    public String getCaption() {
        return "Boring Schieber einfach";
    }

    @Override
    public IRoundSupplier create(String owner) {
        IRoundSupplier mode = new BoringRoundSupplier();
        return mode;
    }
}
