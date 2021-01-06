package ch.taburett.gameworld.endpoints;

import java.util.Map;

public class GameCmd {

    private CMD cmd;

    public CMD getCmd() {
        return cmd;
    }

    public void setCmd(CMD cmd) {
        this.cmd = cmd;
    }

    public Map<String, String> getPayload() {
        return payload;
    }

    public void setPayload(Map<String, String> payload) {
        this.payload = payload;
    }

    private Map<String,String> payload;

    public enum CMD {
        NEW_GAME,
        JOIN,
        PLAY,
        DECIDE
    }
}

