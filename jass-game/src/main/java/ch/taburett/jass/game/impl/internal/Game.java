package ch.taburett.jass.game.impl.internal;

import ch.taburett.jass.game.api.EGameState;
import ch.taburett.jass.game.api.IGameInfo;
import ch.taburett.jass.game.api.IPlayerReference;
import ch.taburett.jass.game.api.ITeam;
import ch.taburett.jass.game.impl.PlayerReference;
import ch.taburett.jass.game.pub.log.ImmutableRound;
import ch.taburett.jass.game.spi.IParmeterizedRound;
import ch.taburett.jass.game.spi.IRoundSupplier;
import ch.taburett.jass.game.spi.impl.PresenterMode;
import ch.taburett.jass.game.spi.events.server.IllegalState;
import ch.taburett.jass.game.spi.events.user.DecideEvent;
import ch.taburett.jass.game.spi.events.user.IUserEvent;
import ch.taburett.jass.game.spi.events.user.Play;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static ch.taburett.jass.game.api.EGameState.*;
import static ch.taburett.jass.game.impl.internal.RoundResponse.*;

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
public class Game implements IGameInfo, ch.taburett.jass.game.api.IGame {

    PlayerReferences r = new PlayerReferences(this);

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

    @Override
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


    @Override
    public List<IPlayerReference> getPlayers() {
        return List.copyOf(r.players);
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
        HashMap<ITeam, Integer> points = roundLog.getPoints();
        return points.values().stream()
                .anyMatch(p -> p >= roundSupplier.getMaxPoints());
    }

    @Override
    public Map<ITeam, Integer> getPoints(ImmutableRound currentRound) {
        ArrayList<ImmutableRound> list = new ArrayList<>(rounds);
        list.add(currentRound);
        RoundLog roundLog = new RoundLog(list);
        HashMap<ITeam, Integer> points = roundLog.getPoints();
        return points;
    }

    @Override
    public List<Map<ITeam, Integer>> getPoints() {
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

    public void accept(IUserEvent<?> event, PlayerReference playerReference) {
        e.execute(() -> accept_(event, playerReference));
    }
    /**
     * Must be Executed on the EventThread
     * @param event
     * @param playerReference
     */
    private void accept_(IUserEvent<?> event, PlayerReference playerReference) {
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

