package ch.taburett.jass.game;

import ch.taburett.jass.game.spi.IParmeterizedRound;
import ch.taburett.jass.game.spi.IRoundSupplier;
import ch.taburett.jass.game.spi.def.PresenterMode;
import ch.taburett.jass.game.spi.messages.DecideEvent;
import ch.taburett.jass.game.spi.messages.IJassMessage;
import ch.taburett.jass.game.spi.messages.Play;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static ch.taburett.jass.game.EGameState.*;
import static ch.taburett.jass.game.RoundResponse.*;
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
public class Game implements IGameInfo {

    PlayerReferences r = new PlayerReferences(this);

    public static final IPlayerReference server = () -> "server";
    private IRoundSupplier roundSupplier;
    private EGameState state = FRESH;

    private int currentPlayerIdx = 0;
    private Round round;
    int selectingPlayer = 0;
    private ArrayList<ImmutableRound> rounds = new ArrayList<>();

    private final ExecutorService e = Executors.newSingleThreadExecutor();
    private RoundPreparer roundPreparer;

    public Game(IRoundSupplier roundSupplier) {
        this.roundSupplier = roundSupplier;
    }

    public void start() {
        e.execute(() -> {
            if( state == FRESH) {
                start_();
            } else {
                throw new IllegalStateException();
            }
        });
    }


    private void prepareRound(int pos) {

        state = PREPARE_ROUND;

        Map<String, PresenterMode> modes = roundSupplier.getModes(rounds);

        roundPreparer = new RoundPreparer(modes,r, this::startRound);
        roundPreparer.prepare(selectingPlayer);

    }

    private void startRound(int pos, IParmeterizedRound mode) {
        state = ROUND;
        round = new Round(r, this, mode);
        round.start();
    }


    public List<PlayerReference> getPlayers() {
        return r.players;
    }

    @Override
    public boolean hasEnded(ImmutableRound currentRound) {
        ArrayList<ImmutableRound> list = new ArrayList<>(rounds);
        list.add(currentRound);
        return hasEnded(list);
    }

    @Override
    public boolean hasEnded(List<ImmutableRound> rounds) {
        RoundLog roundLog = new RoundLog(rounds);
        Map<Team, Integer> points = roundLog.getPoints();
        return points.values().stream()
                .anyMatch(p -> p >= roundSupplier.getMaxPoints());
    }

    @Override
    public Map<Team, Integer> getPoints(ImmutableRound currentRound) {
        ArrayList<ImmutableRound> list = new ArrayList<>(rounds);
        list.add(currentRound);
        RoundLog roundLog = new RoundLog(list);
        Map<Team, Integer> points = roundLog.getPoints();
        return points;
    }

    @Override
    public List<Map<Team, Integer>> getPoints() {
        return rounds.stream()
                .map(ImmutableRound::getTotalPointsByTeam)
                .collect(Collectors.toList());
    }

    public static void sleep() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void accept(IJassMessage event, PlayerReference playerReference) {
        e.execute(() -> accept_(event, playerReference));
    }
    /**
     * Must be Executed on the EventThread
     * @param event
     * @param playerReference
     */
    private void accept_(IJassMessage event, PlayerReference playerReference) {
        if (state == FINISHED) {
            playerReference.sendToUser(new IllegalState("Game is FINISHED "));
        }
        else if( state == PREPARE_ROUND ) {
            if( event instanceof DecideEvent ) {
                roundPreparer.accept((DecideEvent) event);
            } else {
                playerReference.sendToUser(new IllegalState("Only Decision"));
            }
        }
        else if( state == ROUND ) {

            if (round != null && event instanceof Play) {
                var finished = round.accept((Play) event, playerReference);
                if (finished == GAME_ENDED) {
                    state = FINISHED;
                    rounds.add(round.toImmtable());
                } else if (finished == ROUND_ENDED) {
                    rounds.add(round.toImmtable());
                    selectingPlayer += (selectingPlayer + 1) % 4;
                    prepareRound(selectingPlayer);
                }
                System.out.println(new RoundLog(rounds).getPoints());

            } else {
                playerReference.sendToUser(new IllegalState("No Round"));
            }
        }
    }


    private void start_() {
        prepareRound(selectingPlayer);
    }
}

