package ch.taburett.jass.game;

import ch.taburett.jass.cards.DeckUtil;
import ch.taburett.jass.cards.JassCard;
import ch.taburett.jass.game.spi.IRoundSupplier;
import ch.taburett.jass.game.spi.messages.IJassMessage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

import static java.util.stream.Collectors.toList;

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

    PlayerReference server = new PlayerReference("server");
    private IRoundSupplier roundSupplier;
    private IMessageRouter router;
    private ExecutorService executor;
    private EGameState state;

    public Game(IRoundSupplier roundSupplier) {
        this.roundSupplier = roundSupplier;
    }

    public void start(IMessageRouter router) {
        this.router = router;
        router.register(this::receive);
        this.executor = Executors.newSingleThreadExecutor();
        executor.execute( () -> startRound(0));
    }

    public void receive(IJassMessage message) {
        executor.submit(() -> {
            if( message instanceof ChatMessage m) {
                message( m );
            }
        });
    }

    private void startRound(int pos) {

        Round round = new Round(r);

        for (RoundPlayer p : round.roundPlayers) {
            message( new DistributeEvent(server, p.player, p.cards)) ;
        }

        System.out.println("Wating for play...");


    }

    /**
     * Sends message in background via executor thread
     * @param message
     */
    private void message( IJassMessage message )
    {
        executor.execute(() -> this.router.send( message ));
    }

    public List<PlayerReference> getPlayers() {
        return r.players;
    }

    private static class Round {
        private final Map<PlayerReference, List<JassCard>> initalCards;
        private final List<RoundPlayer> roundPlayers;

        Round(PlayerReferences r) {
            var cards_ = DeckUtil.createDeck();
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
                    .collect(toList());


        }
    }

    private static class PlayerReferences {
        public final PlayerReference A1 = new PlayerReference("A1");
        public final PlayerReference A2 = new PlayerReference("A2");
        public final PlayerReference B1 = new PlayerReference("B1");
        public final PlayerReference B2 = new PlayerReference("B2");

        private final List<PlayerReference> players = List.of(A1, B1, A2, B2);

    }

    public static record RoundPlayer (PlayerReference player, List<JassCard> cards) {
        }
}

