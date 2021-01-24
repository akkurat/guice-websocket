package ch.taburett.jass.game.spi.impl;

import ch.taburett.jass.cards.JassCard;
import ch.taburett.jass.cards.JassColor;

import java.util.List;
import java.util.stream.Collectors;

public class LeihUtil {
    public static List<JassCard> legalCards_(List<JassCard> trick, List<JassCard> hand) {
        if (trick.isEmpty()) {
            return hand;
        }
        JassColor color = trick.get(0).color;
        var handByColor = hand.stream()
                .filter(c -> c.color == color)
                .collect(Collectors.toList());
        if (handByColor.isEmpty()) {
            return hand;
        } else {
            return handByColor;
        }
    }
}
