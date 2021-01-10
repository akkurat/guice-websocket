package ch.taburett.jass.game.impl.internal;

import ch.taburett.jass.cards.JassCard;
import ch.taburett.jass.game.impl.PlayerReference;

import java.util.ArrayList;
import java.util.List;

public class RoundPlayer {
    public final PlayerReference player;
    public final ArrayList<JassCard> cards;
    private final List<JassCard> initialCards;

    public RoundPlayer(PlayerReference player, List<JassCard> cards) {
        this.player = player;
        this.initialCards = List.copyOf(cards);
        this.cards = new ArrayList<>(cards);
    }
}
