package ch.taburett.jass.game;

import ch.taburett.jass.game.spi.IParmeterizedRound;
import ch.taburett.jass.game.spi.def.PresenterMode;
import ch.taburett.jass.game.spi.messages.DecideEvent;
import ch.taburett.jass.game.spi.messages.ModeEvent;

import java.util.Map;
import java.util.function.BiConsumer;

public class RoundPreparer {


    private final Map<String, PresenterMode> modes;
    private final PlayerReferences r;
    private final BiConsumer<Integer, IParmeterizedRound> startRoundCallback;
    private int idx;

    public RoundPreparer(Map<String, PresenterMode> modes, PlayerReferences r,
                         BiConsumer<Integer, IParmeterizedRound> startRoundCallback) {
        this.modes = modes;
        this.r = r;
        this.startRoundCallback = startRoundCallback;
    }

    public void prepare(int idx) {
        this.idx = idx;
        r.players.get(idx).sendToUser(new ModeEvent(modes));
    }

    public void accept( DecideEvent message ) {
        DecideEvent.DecideParams pl = message.getPayload();
        PresenterMode factory = modes.get(pl.type);
        IParmeterizedRound turn = factory.getModeFactory().apply(pl.params);
        startRoundCallback.accept(idx, turn);
    }
}
