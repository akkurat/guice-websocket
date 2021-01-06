package ch.taburett.gameworld.endpoints;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GameCmdDelete {


    public final String gameId;

    @JsonCreator( mode = JsonCreator.Mode.PROPERTIES )
    GameCmdDelete(@JsonProperty("gameId") String gameId )
    {
        this.gameId = gameId;
    }

}

