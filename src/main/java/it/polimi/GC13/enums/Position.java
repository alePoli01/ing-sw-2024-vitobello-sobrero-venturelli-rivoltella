package it.polimi.GC13.enums;

/**
 * Enum representing positions in a game. <br>
 * The players in a game are at least two and at most 4,
 * and the positions are assigned clockwise from the {@link #FIRST} position.
 * <ul>
 *     <li>{@link #FIRST} - Represents the first position.</li>
 *     <li>{@link #SECOND} - Represents the second position.</li>
 *     <li>{@link #THIRD} - Represents the third position.</li>
 *     <li>{@link #FOURTH} - Represents the fourth position.</li>
 * </ul>
 * Provides methods to get the next position and convert between integer and enum representation.
 */
public enum Position {

    /**
     * Represents the first position.
     */
    FIRST,

    /**
     * Represents the second position.
     */
    SECOND,

    /**
     * Represents the third position.
     */
    THIRD,

    /**
     * Represents the fourth position.
     */
    FOURTH;


    /**
     * Returns the next position in sequence, considering the number of players in the game.
     *
     * @param playersInGame Number of players in the game.
     * @return The next position enum value.
     */
    public Position next(int playersInGame) {
        if (this == FOURTH) return FIRST;
        if (this == FIRST) return SECOND;
        if (this == SECOND && (playersInGame - 1) == this.ordinal()) return FIRST;
        if (this == THIRD && (playersInGame - 1) == this.ordinal()) return FIRST;
        return values()[this.ordinal() + 1];
    }

    /**
     * Returns the integer representation of the position.
     *
     * @return The integer value corresponding to the position (1 for FIRST, 2 for SECOND, etc.).
     */
    public int getIntPosition() {
        switch (this){
            case FIRST -> {
                return  1;
            }

            case SECOND -> {
                return  2;
            }

            case THIRD -> {
                return  3;
            }

            default -> {
                return 4;
            }
        }
    }

    /**
     * Returns the Position enum value corresponding to the given integer.
     *
     * @param integer The integer value representing the position (1 for FIRST, 2 for SECOND, etc.).
     * @return The Position enum value corresponding to the integer.
     */
    public static Position getPositionFromInt(int integer) {
        switch (integer){
            case 1 -> {
                return  FIRST;
            }

            case 2 -> {
                return SECOND;
            }

            case 3 -> {
                return THIRD;
            }

            default -> {
                return FOURTH;
            }
        }
    }
}
