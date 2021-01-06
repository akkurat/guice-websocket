package ch.taburett.jass.game.spi.events.server;

import ch.taburett.jass.game.spi.impl.PresenterMode;

import java.util.Map;

public class ModeEvent implements IServerMessage<Map<String, PresenterMode>> {
    private final Map<String, PresenterMode> modes;

    public ModeEvent(Map<String, PresenterMode> modes) {
        this.modes = modes;
    }

    @Override
    public String getCode() {
        return "MODE";
    }

    @Override
    public Map<String, PresenterMode> getPayload() {
        return modes;
    }
}
