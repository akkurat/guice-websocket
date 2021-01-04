package ch.taburett.jass.game;

import ch.taburett.jass.cards.JassCard;

import java.util.ArrayList;
import java.util.List;

public class RoundPlayer {
    public final PlayerReference player;
    public final ArrayList<JassCard> cards;

    public RoundPlayer(PlayerReference player, List<JassCard> cards) {
        this.player = player;
        this.cards = new ArrayList<>(cards);
    }
}
