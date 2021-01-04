package ch.taburett.jass.game;

public class Team {
    public final String ref;

    public Team(String ref) {
        this.ref = ref;
    }

    @Override
    public String toString() {
        return "Team: " + ref;
    }
}
