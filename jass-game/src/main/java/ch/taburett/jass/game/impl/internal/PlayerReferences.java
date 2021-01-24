package ch.taburett.jass.game.impl.internal;

import ch.taburett.jass.game.impl.PlayerReference;

import java.util.List;

/**
 * Table
 */
// TODO: rename to Table
class PlayerReferences {
    private final Game game;
    public final Team A = new Team("A");
    public final Team B = new Team("B");
    public final PlayerReference A1;
    public final PlayerReference A2;
    public final PlayerReference B1;
    public final PlayerReference B2;

    final List<PlayerReference> players;

    public PlayerReferences(Game game) {
        this.game = game;
        A1 = new PlayerReference(game, "A1", A);
        A2 = new PlayerReference(game, "A2", A);
        B1 = new PlayerReference(game, "B1", B);
        B2 = new PlayerReference(game, "B2", B);

        players = List.of(A1, B1, A2, B2);
    }

}
