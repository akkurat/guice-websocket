package ch.taburett.jass.game;

public record PlayerReference(String getRef, Type getType) {
    public PlayerReference(String getRef) {
        this(getRef, Type.PLAYER);
    }

    static enum Type  {
            PLAYER, OBSERVER, MIGHTYOBSERVER, SERVER
    }
}
