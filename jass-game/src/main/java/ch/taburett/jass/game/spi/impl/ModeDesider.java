package ch.taburett.jass.game.spi.impl;

import ch.taburett.jass.game.pub.log.ImmutableRound;
import ch.taburett.jass.game.spi.IRoundSupplier;

import java.util.ArrayList;
import java.util.Map;

import static ch.taburett.jass.cards.JassColor.*;

public class ModeDesider implements IRoundSupplier {
    @Override
    public Map<String, PresenterMode> getModes(ArrayList<ImmutableRound> rounds) {
        return Map.of(
                "mi", new PresenterMode("Misere", (m) -> new Misere(3)),
                "ob", new PresenterMode("Obenabe", (m) -> new ObenAbe(3)),
//                "un", new PresenterMode("Obenabe", (m) -> new ObenAbe(3)),
                "ht", new PresenterMode("Herz Trumpf", (m) -> new Trump(HERZ, 1)),
                "et", new PresenterMode("Ecken Trumpf", (m) -> new Trump(ECKEN, 1)),
                "st", new PresenterMode("Schaufel Trumpf", (m) -> new Trump(SCHAUFEL, 2)),
                "kt", new PresenterMode("Kreuz Trumpf", (m) -> new Trump(KREUZ, 2))
        );
    }

    @Override
    public int getMaxPoints() {
        return 2500;
    }


}
