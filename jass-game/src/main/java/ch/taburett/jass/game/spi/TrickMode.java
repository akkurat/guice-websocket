package ch.taburett.jass.game.spi;

import lombok.Data;

@Data
public class TrickMode {
    private final String caption;
    private final String trickCaption;
    private final int factor;

}
