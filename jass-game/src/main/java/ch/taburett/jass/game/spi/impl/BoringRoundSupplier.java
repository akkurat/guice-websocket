package ch.taburett.jass.game.spi.impl;

import ch.taburett.jass.game.pub.log.ImmutableRound;
import ch.taburett.jass.game.spi.IRoundSupplier;
import com.google.common.collect.ImmutableMap;

import java.util.ArrayList;
import java.util.Map;

import static ch.taburett.jass.cards.JassColor.*;

public class BoringRoundSupplier implements IRoundSupplier {
    @Override
    public Map<String, PresenterMode> getModes(ArrayList<ImmutableRound> rounds) {
        return ImmutableMap.of(
                "ht", new PresenterMode("Herz Trumpf", m -> new Trump(HERZ, 1)),
                "et", new PresenterMode("Ecken Trumpf", m -> new Trump(ECKEN, 1)),
                "st", new PresenterMode("Schaufel Trumpf", m -> new Trump(SCHAUFEL, 2)),
                "kt", new PresenterMode("Kreuz Trumpf", m -> new Trump(KREUZ, 2)),
                "ob", new PresenterMode("Obenabe", m -> new ObenAbe(3))

        );
    }

    @Override
    public int getMaxPoints() {
        return 1000;
    }


}
