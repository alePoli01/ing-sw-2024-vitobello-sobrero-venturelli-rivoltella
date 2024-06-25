package it.polimi.GC13.enums;

/**
 * Enum representing the different states of the game.
 * <ul>
 *     <li>{@link #JOINING} - Players are joining the game.</li>
 *     <li>{@link #SETUP} - Game setup phase.</li>
 *     <li>{@link #DEALING_CARDS} - Dealing cards phase.</li>
 *     <li>{@link #MID} - Mid-game phase.</li>
 *     <li>{@link #END} - End of the game.</li>
 * </ul>
 */
public enum GameState {

    /**
     * Represents the state when players are joining the game (<i>login phase</i>).
     */
    JOINING,

    /**
     * Represents the setup phase of the game 
     * (<i>token setup phase</i>, <i>starter card setup phase</i>, <i>objective card setup phase</i>).
     */
    SETUP,

    /**
     * Represents the phase when cards are being dealt.
     */
    DEALING_CARDS,

    /**
     * Represents the mid-game phase (<i>the game itself</i>).
     */
    MID,

    /**
     * Represents the end state of the game.
     */
    END
}
