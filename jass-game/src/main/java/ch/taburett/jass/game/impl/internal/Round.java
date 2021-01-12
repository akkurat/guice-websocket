package ch.taburett.jass.game.impl.internal;

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
import ch.taburett.jass.game.spi.events.user.Play;

import java.util.ArrayList;
import java.util.List;

import static ch.taburett.jass.game.impl.internal.RoundResponse.*;
import static java.util.stream.Collectors.toMap;

// -> RoundGame / Play
// container Class including mode selection should be called round
class Round {
    private Trick trick;
    private RoundPlayers roundPlayers;
    private IGameInfo gameInfo;
    private IParmeterizedRound mode;

    private ArrayList<GenericImmutableTrick> turnLog = new ArrayList<GenericImmutableTrick>();

    Round(RoundPlayers roundPlayers, IGameInfo gameInfo, IParmeterizedRound mode) {
        this.roundPlayers = roundPlayers;
        this.gameInfo = gameInfo;
        this.mode = mode;
    }

    void start(int pos) {

        System.out.println("Wating for play...");

        trick = new Trick(roundPlayers.size());

        sendStatus(toImmtable());

    }

    public RoundResponse accept(Play move, PlayerReference playerReference) {
        var playerOnTurn = roundPlayers.getCurrentPlayer();
        if (playerReference != playerOnTurn) {
            playerReference.sendToUser(new IllegalState(playerReference, "Not your Turn. Patience!"));
            return ILLEGAL;
        }
        var rp = roundPlayers.getRoundPlayer(playerOnTurn);
        JassCard card = move.getPayload();
        rp.cards.remove(card);

        trick.play(playerReference, card);


        var tmpTurns = new ArrayList<>(turnLog);
        tmpTurns.add(trick.getImmutableTrick());
        ImmutableRound tmpRound = toImmtable(tmpTurns);

        sendStati(null, tmpRound);


        if (gameInfo.hasEnded(tmpRound)) {
            turnLog.add(trick.getImmutableTrick());
            return GAME_ENDED;
        }

        if (roundPlayers.allCardsPlayed()) {
            turnLog.add(trick.getImmutableTrick());
            return ROUND_ENDED;
        }

        if( trick.hasEnded() )  {
            GenericImmutableTrick imTrick = trick.getImmutableTrick();
            IPlayerReference nextPlayer = imTrick.whoTakes(mode.getRankMode(turnLog.size()));
            turnLog.add(imTrick);
            trick = new Trick(roundPlayers.size());
            roundPlayers.setPlayer(nextPlayer);
        } else {
            roundPlayers.next();
        }

        Game.sleep();


        sendStatus(tmpRound);

        return trick.isFresh() ? TURN_ENDED : TURN_CONTINUE;

    }

    private void sendStatus(ImmutableRound tmpRound) {
        var np = roundPlayers.getCurrentRoundPlayer();

        sendStati(np, tmpRound);

    }

    private void sendStati(RoundPlayer np, ImmutableRound tmpRound) {
        for (RoundPlayer rp_ : roundPlayers.roundPlayers()) {
            System.out.println("Sending stati");
            StatusPayload payload = new StatusPayload(
                    rp_.cards,
                    mode.getRankMode(turnLog.size()).legalCards(trick.getCards(), rp_.cards),
                    trick.log, rp_ == np,
                    gameInfo.getPoints(tmpRound),
                    mode.getTrickMode(turnLog.size()),
                    gameInfo.getLog());
            rp_.player.sendToUser(new Status(rp_.player,payload));
        }
    }

    private ImmutableRound toImmtable(List<GenericImmutableTrick> roundLog) {
        return new ImmutableRound(roundLog, mode);
    }

    public ImmutableRound toImmtable() {
        return new ImmutableRound(turnLog, mode);
    }
}
