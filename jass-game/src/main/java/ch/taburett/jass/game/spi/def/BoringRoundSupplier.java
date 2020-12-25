package ch.taburett.jass.game.spi.def;

import ch.taburett.jass.game.spi.IRound;
import ch.taburett.jass.game.spi.IRoundSupplier;

import java.util.List;
import java.util.Map;

import static ch.taburett.jass.cards.JassColor.*;

public class BoringRoundSupplier implements IRoundSupplier {
    public Map<String, PresenterMode> getModes(List<IRound> rounds) {
        return Map.of(
                "ht", new PresenterMode("Herz Trumpf", () -> new Trump(HERZ)),
                "et", new PresenterMode("Ecken Trumpf", () -> new Trump(ECKEN)),
                "st", new PresenterMode("Schaufel Trumpf", () -> new Trump(SCHAUFEL)),
                "kt", new PresenterMode("Kreuz Trumpf", () -> new Trump(KREUZ)),
                "ob", new PresenterMode("Kreuz Trumpf", () -> new ObenAbeCount())

        );
    }



}
