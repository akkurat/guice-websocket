package ch.taburett.jass.game.impl.internal;

import ch.taburett.jass.game.api.ITeam;

public class Team implements ITeam {
    @Override
    public String getRef() {
        return ref;
    }

    public final String ref;

    public Team(String ref) {
        this.ref = ref;
    }

    @Override
    public String toString() {
        return "Team: " + ref;
    }
}
