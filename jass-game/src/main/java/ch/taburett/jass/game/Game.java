package ch.taburett.jass.game;

import ch.taburett.jass.cards.DeckUtil;
import ch.taburett.jass.cards.JassCard;
import ch.taburett.jass.game.spi.IRoundSupplier;
import ch.taburett.jass.game.spi.messages.*;

import java.lang.ref.Reference;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Consumer;

import static ch.taburett.jass.game.EGameState.FINISHED;
import static java.util.stream.Collectors.reducing;
import static java.util.stream.Collectors.toMap;

/**
 * Players
 * <pre>
 *      A1
 *   B1    B2
 *      A2
 * </pre>
 * Players are initial on Conctructor
 * Players can be Changed on the outside
 */
public class Game {

    PlayerReferences r = new PlayerReferences();

    public static final IPlayerReference server = () -> "server";
    private IRoundSupplier roundSupplier;
    private EGameState state;

    private int currentPlayerIdx = 0;
    private Round round;

    public Game(IRoundSupplier roundSupplier) {
        this.roundSupplier = roundSupplier;
    }

    public void start() {
        startRound(0);
    }


    private void startRound(int pos) {

        // TODO: Select mode

        round = new Round(r);
        round.start();
    }


    public List<PlayerReference> getPlayers() {
        return r.players;
    }

    private static class Round {
        private final Map<PlayerReference, List<JassCard>> initalCards;
        private final Map<PlayerReference, RoundPlayer> roundPlayers;
        private Turn turn;
        private PlayerReferences r;
        private int currentPlayerIdx;

        Round(PlayerReferences r) {
            this.r = r;
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

        private void start() {
            for (RoundPlayer p : roundPlayers.values()) {
                p.player.sendToUser(new DistributeEvent(server, p.player, p.cards));
            }

            System.out.println("Wating for play...");

            turn = new Turn();

            sendStatus();

        }

        public void accept(Play move, PlayerReference playerReference) {
            var playerOnTurn = r.players.get(currentPlayerIdx);
            if (playerReference != playerOnTurn) {
                playerReference.sendToUser(new IllegalState("Not your Turn. Patience!"));
                return;
            }
            var rp = roundPlayers.get(playerReference);
            JassCard card = move.getPayload();
            rp.cards.remove(card);

            turn.play(playerReference, card);
            sendStati(null);

            sleep();
            turn = turn.nextTurn();

            currentPlayerIdx = (currentPlayerIdx + 1) % 4;

            sendStatus();
        }

        private void sendStatus() {
            var ref = r.players.get(currentPlayerIdx);
            var np = roundPlayers.get(ref);

            sendStati(np);

        }

        private void sendStati(RoundPlayer np) {
            for (RoundPlayer rp_ : roundPlayers.values()) {
                System.out.println("Sending stati");
                rp_.player.sendToUser(
                        new Status(new StatusPayload(rp_.cards, turn.log, rp_ == np))
                );
            }
        }
    }

    public static void sleep() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private class PlayerReferences {
        public final PlayerReference A1 = new PlayerReference("A1");
        public final PlayerReference A2 = new PlayerReference("A2");
        public final PlayerReference B1 = new PlayerReference("B1");
        public final PlayerReference B2 = new PlayerReference("B2");

        private final List<PlayerReference> players = List.of(A1, B1, A2, B2);

    }

    public static class RoundPlayer {
        public final PlayerReference player;
        public final ArrayList<JassCard> cards;

        public RoundPlayer(PlayerReference player, List<JassCard> cards) {
            this.player = player;
            this.cards = new ArrayList<>(cards);
        }
    }

    public class PlayerReference implements IPlayerReference {

        @Override
        public String getRef() {
            return ref;
        }

        private final String ref;
        private Consumer<IJassMessage> proxy;

        PlayerReference(String ref) {
            this.ref = ref;
        }

        public void setProxy(Consumer<IJassMessage> proxy) {
            this.proxy = proxy;
        }

        public void sendToUser(IJassMessage event) {
            if (proxy == null) {
                throw new IllegalStateException("Proxy must always be present");
            }
            proxy.accept(event);
        }

        public void sendToServer(Play event) {
            Game.this.accept(event, this);
        }


    }

    private void accept(Play event, PlayerReference playerReference) {
        if (state == FINISHED) {
            playerReference.sendToUser(new IllegalState("Game is FINISHED "));
        }

        if (round != null) {
            round.accept(event, playerReference);
        } else {
            playerReference.sendToUser(new IllegalState("No Round"));
        }
    }

}

