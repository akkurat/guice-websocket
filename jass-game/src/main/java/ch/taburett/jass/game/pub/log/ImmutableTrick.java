package ch.taburett.jass.game.pub.log;

import ch.taburett.jass.game.api.IPlayerReference;
import lombok.Data;

import java.util.List;

@Data
public class ImmutableTrick {
    private final List<ImmutableLogEntry> log;
    private final IPlayerReference whoTakes;
    private final int sum;
    private final String trickCaption   ;

}
