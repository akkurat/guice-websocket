package ch.taburett.jass.game;

import ch.taburett.jass.cards.DeckUtil;
import ch.taburett.jass.cards.JassCard;
import ch.taburett.jass.game.spi.IRoundSupplier;
import ch.taburett.jass.game.spi.messages.IJassMessage;
import ch.taburett.jass.game.spi.messages.Play;
import ch.taburett.jass.game.spi.messages.YourTurn;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.function.Consumer;

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
    private ExecutorService executor;
    private EGameState state;

    private int currentPlayerIdx = 0;
    private Round round;

    public Game(IRoundSupplier roundSupplier) {
        this.roundSupplier = roundSupplier;
    }

    public void start() {
        this.executor = Executors.newSingleThreadExecutor();
        executor.execute( () -> startRound(0));
    }

    private void startRound(int pos) {

        round = new Round(r);

        for (RoundPlayer p : round.roundPlayers) {
            p.player.sendToUser( new DistributeEvent(server, p.player, p.cards)) ;
        }

        System.out.println("Wating for play...");

        RoundPlayer roundPlayer = round.roundPlayers.get(currentPlayerIdx);
        roundPlayer.player.sendToUser(
                new YourTurn( new YourTurn.Payload(roundPlayer.cards, null)) );

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

    private class PlayerReferences {
        public final PlayerReference A1 = new PlayerReference("A1");
        public final PlayerReference A2 = new PlayerReference("A2");
        public final PlayerReference B1 = new PlayerReference("B1");
        public final PlayerReference B2 = new PlayerReference("B2");

        private final List<PlayerReference> players = List.of(A1, B1, A2, B2);

    }

    public static record RoundPlayer (PlayerReference player, List<JassCard> cards) {
        }

    public class PlayerReference {
        public String getRef() {
            return ref;
        }

        private final String ref;
        private Consumer<IJassMessage> proxy  ;

        PlayerReference(String ref) {
            this.ref = ref;
        }

        public void setProxy(Consumer<IJassMessage> proxy) {
            this.proxy = proxy;
        }
        public void sendToUser(IJassMessage event) {
            if(proxy == null )
            {
                throw new IllegalStateException("Proxy must always be present");
            }
            proxy.accept(event);
        }
        public void sendToServer(IJassMessage event) {
            Game.this.receive(this, event);
        }

    }

    private void receive(PlayerReference playerReference, IJassMessage event) {
        if(getPlayers().indexOf( playerReference ) == currentPlayerIdx )
        {
            if(event instanceof Play p) {
                var rp = round.roundPlayers.get(currentPlayerIdx);
                if( rp.player != playerReference ) {
                    throw new IllegalStateException("shit");
                }
                rp.cards.remove(p.getPayload());
                currentPlayerIdx = (currentPlayerIdx+1)%4;
                var np = round.roundPlayers.get(currentPlayerIdx);
                executor.submit(() -> np.player.sendToUser(
                        new YourTurn(new YourTurn.Payload(np.cards, null )) ));
            }
        }
    }

}

