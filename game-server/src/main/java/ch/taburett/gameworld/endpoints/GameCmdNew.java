package ch.taburett.gameworld.endpoints;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class GameCmdNew {

    public final String type;

    @JsonCreator( mode = JsonCreator.Mode.PROPERTIES )
    GameCmdNew(@JsonProperty("type") String type )
    {
        this.type = type;
    }

}

