package it.polimi.GC13.enums;

/**
 * Enum representing different points conditions for the golden cards.
 * <ul>
 *     <li>{@link #INKWELL} - Represents an inkwell condition.</li>
 *     <li>{@link #MANUSCRIPT} - Represents a manuscript condition.</li>
 *     <li>{@link #QUILL} - Represents a quill condition.</li>
 *     <li>{@link #EDGE} - Represents an edge condition.</li>
 *     <li>{@link #NULL} - Represents a null condition.</li>
 * </ul>
 * Each enum constant has a corresponding string representation for printing in gold color.
 */
public enum PointsCondition {

    /**
     * Represents the inkwell condition.
     */
    INKWELL,

    /**
     * Represents the manuscript condition.
     */
    MANUSCRIPT,

    /**
     * Represents the quill condition.
     */
    QUILL,

    /**
     * Represents the edge condition.
     */
    EDGE,

    /**
     * Represents a null condition.
     */
    NULL;


    final String gold = "\u001b[93m"; // gold color ANSI escape code
    final String reset = "\u001b[0m"; // reset ANSI escape code


    /**
     * Returns the string representation of the enum constant.
     *
     * @return A string representing the enum constant in gold color.
     */
    public String toString(){
        switch (this){
            case EDGE -> {
                return gold+"E"+reset;
            }
            case INKWELL -> {
                return gold+"I"+reset;
            }
            case QUILL -> {
                return gold+"Q"+reset;
            }
            case MANUSCRIPT -> {
                return gold+"M"+reset;
            }
            case NULL->{
                return " ";
            }

        }
        return null;
    }
}

