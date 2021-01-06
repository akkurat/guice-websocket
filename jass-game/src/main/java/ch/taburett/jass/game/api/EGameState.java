package ch.taburett.jass.game.api;

/**
 * Redundandant. Basically
 * by convention (preparer == null, round == null, finished==true)
 * we could do the same thing.
 * however for now it seems more intuitive having
 * an explicit state
 * Replace Enum By Interface and different State Handlers
 */
public enum EGameState {
    FRESH,
    PREPARE_ROUND,
    ROUND,
    FINISHED
}
