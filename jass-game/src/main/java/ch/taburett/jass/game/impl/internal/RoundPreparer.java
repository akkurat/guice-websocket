package ch.taburett.jass.game.impl.internal;

import ch.taburett.jass.game.impl.PlayerReference;
import ch.taburett.jass.game.spi.IParmeterizedRound;
import ch.taburett.jass.game.spi.impl.PresenterMode;
import ch.taburett.jass.game.spi.events.user.DecideEvent;
import ch.taburett.jass.game.spi.events.server.ModeEvent;

import java.util.Map;
import java.util.function.BiConsumer;

public class RoundPreparer {


    private final Map<String, PresenterMode> modes;
    private final BiConsumer<Integer, IParmeterizedRound> startRoundCallback;
    private final RoundPlayers roundPlayers;
    private int idx;

    public RoundPreparer(Map<String, PresenterMode> modes,
                         BiConsumer<Integer, IParmeterizedRound> startRoundCallback, RoundPlayers roundPlayers) {
        this.modes = modes;
        this.startRoundCallback = startRoundCallback;
        this.roundPlayers = roundPlayers;
    }

    public void prepare() {
        for (RoundPlayer p : roundPlayers.rpByRef.values()) {
            var modePayload  = new ModeEvent.ModePayload(modes,p.cards,roundPlayers.getCurrentPlayer());
            p.player.sendToUser(new ModeEvent(p.player,modePayload));
        }
    }

    public void accept(DecideEvent message, PlayerReference playerReference) {
        if(roundPlayers.getCurrentPlayer()==playerReference) {
            DecideEvent.DecideParams pl = message.getPayload();
            PresenterMode factory = modes.get(pl.type);
            IParmeterizedRound turn = factory.getModeFactory().createRound(pl.params);
            startRoundCallback.accept(idx, turn);
        }
    }
}
