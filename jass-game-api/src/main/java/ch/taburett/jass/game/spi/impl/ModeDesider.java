package ch.taburett.jass.game.spi.impl;

import ch.taburett.jass.game.pub.log.ImmutableRound;
import ch.taburett.jass.game.spi.IRoundSupplier;
import com.google.common.collect.ImmutableMap;

import java.util.ArrayList;
import java.util.Map;

import static ch.taburett.jass.cards.JassColor.*;
import static ch.taburett.jass.game.spi.impl.Slalom.EStart.OBE;
import static ch.taburett.jass.game.spi.impl.Slalom.EStart.UNDE;

public class ModeDesider implements IRoundSupplier {
    @Override
    public Map<String, PresenterMode> getModes(ArrayList<ImmutableRound> rounds) {
        return ImmutableMap.<String,PresenterMode>builder()
                .put("ht", new PresenterMode("Herz Trumpf", (m) -> new Trump(HERZ, 1)))
                .put("et", new PresenterMode("Ecken Trumpf", (m) -> new Trump(ECKEN, 1)))
                .put("st", new PresenterMode("Schaufel Trumpf", (m) -> new Trump(SCHAUFEL, 2)))
                .put("kt", new PresenterMode("Kreuz Trumpf", (m) -> new Trump(KREUZ, 2)))
                .put("ob", new PresenterMode("Obenabe", (m) -> new ObenAbe(3)))
                .put("un", new PresenterMode("Undenufe", (m) -> new Undenufe(3)))
                .put("slo", new PresenterMode("Slalom obe", (m) -> new Slalom(3, OBE)))
                .put("slu", new PresenterMode("Slalom unde", (m) -> new Slalom(3, UNDE)))
                .put("mi", new PresenterMode("Misere", (m) -> new Misere(3)))
                .build();
    }

    @Override
    public int getMaxPoints() {
        return 2500;
    }


}
