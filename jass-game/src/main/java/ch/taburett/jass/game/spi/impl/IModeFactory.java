package ch.taburett.jass.game.spi.impl;

import ch.taburett.jass.game.spi.IParmeterizedRound;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public interface IModeFactory {
    public IParmeterizedRound createRound(Map<String,String> params);
}
