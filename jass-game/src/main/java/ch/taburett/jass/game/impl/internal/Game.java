package ch.taburett.jass.game.impl.internal;

import ch.taburett.jass.game.api.EGameState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
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

    Logger logger = LoggerFactory.getLogger(Game.class);
    private final ThreadFactory threadFactory;
    PlayerReferences r = new PlayerReferences(this);

    private IRoundSupplier roundSupplier;
    private EGameState state = FRESH;

    private Round round;
    private ArrayList<ImmutableRound> rounds = new ArrayList<>();



    private final ScheduledExecutorService e;
    private RoundPreparer roundPreparer;
    private Thread lastThread;
    private RoundPlayers roundPlayers;
    // IMP:ROUND_CONFIG
    private int startPlayerIdx = 0;

    public Game(IRoundSupplier roundSupplier) {

        this.roundSupplier = roundSupplier;
        this.threadFactory = Executors.defaultThreadFactory();
        e = Executors.newSingleThreadScheduledExecutor(this::getThread);
    }

    private Thread getThread(Runnable runnable) {
        lastThread = threadFactory.newThread(runnable);
        return lastThread;
    }


    @Override
    public void start() {
         execute(() -> {
            if( state == FRESH) {
                start_();
            } else {
                throw new IllegalStateException();
            }
        });



    }


    private void prepareRound(int idx) {
        checkThread();
        state = PREPARE_ROUND;

        Map<String, PresenterMode> modes = roundSupplier.getModes(rounds);

        PlayerReference pl = r.players.get(idx);

        // IMP:ROUND_CONFIG -> Nextplayer Can be influenced from the Outside
        roundPlayers = new RoundPlayers(r,pl);

        roundPreparer = new RoundPreparer(modes, this::startRound,roundPlayers);
        roundPreparer.prepare();

    }


    private void startRound(IPlayerReference ref, IParmeterizedRound mode) {
        checkThread();
        state = ROUND;
        round = new Round(roundPlayers, this, mode);
        round.start(ref);
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
        if(currentRound != null) {
            list.add(currentRound);
        }
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

    @Override
    public List<ImmutableRound> getLog() {
        return List.copyOf(rounds);
    }

    public static void sleep() {
//        try {
//            Thread.sleep(1);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }

    public void accept(IUserEvent<?> event, PlayerReference playerReference) {
        execute(() -> accept_(event, playerReference));
    }

    private void execute(Runnable runnable) {
        e.execute(() -> {
            try {
                runnable.run();
            } catch (Throwable t) {
                logger.error("GameThread Error", t);
            }
            ;
        });
    }

    /**
     * Must be Executed on the EventThread
     * @param event
     * @param playerReference
     */
    private void accept_(IUserEvent<?> event, PlayerReference playerReference) {
        checkThread();
        if (state == FINISHED) {
            playerReference.sendToUser(new IllegalState(playerReference,"Game is FINISHED "));
        }
        else if( state == PREPARE_ROUND ) {
            if( event instanceof DecideEvent ) {
                roundPreparer.accept((DecideEvent) event, playerReference);
            } else {
                playerReference.sendToUser(new IllegalState(playerReference, "Only Decision"));
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
                    // IMP:ROUND_CONFIG
                    startPlayerIdx = (startPlayerIdx+1)%r.players.size();
                    prepareRound(startPlayerIdx);
                }
                //System.out.println(new RoundLog(rounds).getPoints());

            } else {
                playerReference.sendToUser(new IllegalState(playerReference, "No Round"));
            }
        }
    }


    private void start_() {
        prepareRound(0);
    }
    private void checkThread() {
        if (Thread.currentThread() != lastThread) {
            System.err.println("Shit");
        }
    }
}

