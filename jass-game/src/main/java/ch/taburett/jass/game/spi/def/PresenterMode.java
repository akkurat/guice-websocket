package ch.taburett.jass.game.spi.def;

import ch.taburett.jass.game.spi.IParmeterizedRound;

import java.util.function.Supplier;

public record PresenterMode(String getDescription, Supplier<IParmeterizedRound> modeFactory )
{

}

