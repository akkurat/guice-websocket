package ch.taburett.jass.game.impl.internal;

import ch.taburett.jass.cards.DeckUtil;
import ch.taburett.jass.cards.JassCard;
import ch.taburett.jass.game.api.IGameInfo;
import ch.taburett.jass.game.api.IPlayerReference;
import ch.taburett.jass.game.impl.PlayerReference;
import ch.taburett.jass.game.pub.log.ImmutableRound;
import ch.taburett.jass.game.pub.log.GenericImmutableTrick;
import ch.taburett.jass.game.spi.IParmeterizedRound;
import ch.taburett.jass.game.spi.events.server.IllegalState;
import ch.taburett.jass.game.spi.events.server.Status;
import ch.taburett.jass.game.spi.events.server.StatusPayload;
import ch.taburett.jass.game.spi.events.server.DistributeEvent;
import ch.taburett.jass.game.spi.events.user.Play;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static ch.taburett.jass.game.impl.internal.RoundResponse.*;
import static java.util.stream.Collectors.toMap;

class Round {
    private final Map<PlayerReference, List<JassCard>> initalCards;
    private final Map<PlayerReference, RoundPlayer> roundPlayers;
    private Trick trick;
    private PlayerReferences r;
    private IGameInfo gameInfo;
    private IParmeterizedRound mode;
    private int currentPlayerIdx;
    private ArrayList<GenericImmutableTrick> turns = new ArrayList<GenericImmutableTrick>();

    Round(PlayerReferences r, IGameInfo gameInfo, IParmeterizedRound mode) {
        this.r = r;
        this.gameInfo = gameInfo;
        this.mode = mode;
        var cards_ = DeckUtil.getInstance().createDeck();
        var cards = new ArrayList<>(cards_);
        Collections.shuffle(cards);

        this.initalCards = Map.of(
                r.A1, cards.subList(0, 9),
                r.B1, cards.subList(9, 18),
                r.A2, cards.subList(18, 27),
                r.B2, cards.subList(27, 36)
        );

        this.roundPlayers = initalCards.entrySet().stream()
                .map(e -> new RoundPlayer(e.getKey(), e.getValue()))
                .collect(toMap(rp -> rp.player, rp -> rp));
    }

    void start(int pos) {
        for (RoundPlayer p : roundPlayers.values()) {
            p.player.sendToUser(new DistributeEvent(p.cards));
        }

        System.out.println("Wating for play...");

        trick = new Trick(roundPlayers.size());

        sendStatus(toImmtable());

    }

    public RoundResponse accept(Play move, PlayerReference playerReference) {
        var playerOnTurn = r.players.get(currentPlayerIdx);
        if (playerReference != playerOnTurn) {
            playerReference.sendToUser(new IllegalState("Not your Turn. Patience!"));
            return ILLEGAL;
        }
        var rp = roundPlayers.get(playerReference);
        JassCard card = move.getPayload();
        rp.cards.remove(card);

        trick.play(playerReference, card);


        var tmpTurns = new ArrayList<>(turns);
        tmpTurns.add(trick.getImmutableTrick());
        ImmutableRound tmpRound = toImmtable(tmpTurns);

        sendStati(null, tmpRound);


        if (gameInfo.hasEnded(tmpRound)) {
            turns.add(trick.getImmutableTrick());
            return GAME_ENDED;
        }

        if (roundPlayers.values().stream()
                .allMatch(p -> p.cards.isEmpty())) {
            turns.add(trick.getImmutableTrick());
            return ROUND_ENDED;
        }

        if( trick.hasEnded() )  {
            GenericImmutableTrick imTrick = trick.getImmutableTrick();
            turns.add(imTrick);
            IPlayerReference nextPlayer = imTrick.whoTakes(mode.getRankMode());
            currentPlayerIdx = r.getIdxOfPlayer( nextPlayer.getRef() );
            trick = new Trick(r.players.size());
        } else {
            currentPlayerIdx = r.next(currentPlayerIdx);
        }

        Game.sleep();


        sendStatus(tmpRound);

        return trick.isFresh() ? TURN_ENDED : TURN_CONTINUE;

    }

    private void sendStatus(ImmutableRound tmpRound) {
        var ref = r.players.get(currentPlayerIdx);
        var np = roundPlayers.get(ref);

        sendStati(np, tmpRound);

    }

    private void sendStati(RoundPlayer np, ImmutableRound tmpRound) {
        for (RoundPlayer rp_ : roundPlayers.values()) {
            System.out.println("Sending stati");
            StatusPayload payload = new StatusPayload(
                    rp_.cards,
                    mode.getRankMode().legalCards(trick.getCards(), rp_.cards),
                    trick.log, rp_ == np,
                    gameInfo.getPoints(tmpRound), mode,
                    gameInfo.getLog());
            rp_.player.sendToUser(new Status(payload));
        }
    }

    private ImmutableRound toImmtable(List<GenericImmutableTrick> roundLog) {
        return new ImmutableRound(initalCards, roundLog, mode);
    }

    public ImmutableRound toImmtable() {
        return new ImmutableRound(initalCards, turns, mode);
    }
}
