package ch.taburett.jass.game.impl.internal;

import ch.taburett.jass.cards.DeckUtil;
import ch.taburett.jass.game.impl.PlayerReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toUnmodifiableMap;

public class RoundPlayers {
    public final Map<PlayerReference, RoundPlayer> rpByRef;
    private final Map<RoundPlayer, PlayerReference> refByRp;
    private final List<PlayerReference> references;
    private final List<RoundPlayer> rPlayers;
    private int idx;

    public RoundPlayers(PlayerReferences r, PlayerReference startPlayer) {

        var deck = DeckUtil.getInstance().createDeck();
        var cards = new ArrayList<>(deck);
        Collections.shuffle(cards);

        var cardsPerPlayer = Map.of(
                r.A1, cards.subList(0, 9),
                r.B1, cards.subList(9, 18),
                r.A2, cards.subList(18, 27),
                r.B2, cards.subList(27, 36)
        );

        this.rpByRef = cardsPerPlayer.entrySet().stream()
                .map(e -> new RoundPlayer(e.getKey(), e.getValue()))
                .collect(toUnmodifiableMap(rp -> rp.player, rp -> rp));
        this.refByRp = rpByRef.entrySet().stream().collect(Collectors.toUnmodifiableMap(Map.Entry::getValue, Map.Entry::getKey));

        this.references = List.copyOf(rpByRef.keySet());
        this.rPlayers = List.copyOf(rpByRef.values());

        this.idx = references.indexOf( startPlayer );
    }

    public int size() {
        return rpByRef.size();
    }

    private PlayerReference get(int idx) {
        return references.get(idx);
    }

    private RoundPlayer getRp(int idx) {
        return rPlayers.get(idx);
    }

    public PlayerReference getCurrentPlayer() {
        return references.get(idx);
    }

    public RoundPlayer getCurrentRoundPlayer() {
        return rPlayers.get(idx);
    }

    /**
     * sets next Player and returns the set player
     * @return
     */
    public PlayerReference next() {
        idx = (idx+1)%references.size();
        return getCurrentPlayer();
    }

    public RoundPlayer getRoundPlayer(PlayerReference playerOnTurn) {
        return rpByRef.get(playerOnTurn);
    }

    public boolean allCardsPlayed() {
        return rPlayers.stream().allMatch(p -> p.cards.isEmpty());
    }

    public List<RoundPlayer> roundPlayers() {
        return rPlayers;
    }
}
