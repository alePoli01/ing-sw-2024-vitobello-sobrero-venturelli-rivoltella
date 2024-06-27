package it.polimi.GC13.enums;

/**
 * Enum representing different types of resources in the game.
 * <ul>
 *     <li>{@link #NULL} - Represents the hidden corner of a playable card.</li>
 *     <li>{@link #EMPTY} - Represents an empty corner of a playable card.</li>
 *     <li>{@link #ANIMAL} - Represents an animal reign resource.</li>
 *     <li>{@link #FUNGI} - Represents a fungi reign resource.</li>
 *     <li>{@link #INSECT} - Represents an insect reign resource.</li>
 *     <li>{@link #PLANT} - Represents a plant reign resource.</li>
 *     <li>{@link #INKWELL} - Represents an inkwell object resource.</li>
 *     <li>{@link #MANUSCRIPT} - Represents a manuscript object resource.</li>
 *     <li>{@link #QUILL} - Represents a quill object resource.</li>
 * </ul>
 *
 * Provides methods to check if a resource is a type of <b>reign</b> (<i>animal</i>, <i>fungi</i>, <i>insect</i>, <i>plant</i>)
 * or an <b>object</b> (<i>inkwell</i>, <i>manuscript</i>, <i>quill</i>),
 * and a custom string representation with ANSI escape codes for color highlighting.
 */
public enum Resource {

    /**
     * Represents a hidden corner of a playable card.
     */
    NULL,

    /**
     * Represents an empty corner of a playable card.
     */
    EMPTY,

    /**
     * Represents an animal reign resource.
     */
    ANIMAL,

    /**
     * Represents a fungi reign resource.
     */
    FUNGI,

    /**
     * Represents an insect reign resource.
     */
    INSECT,

    /**
     * Represents a plant reign resource.
     */
    PLANT,

    /**
     * Represents an inkwell object resource.
     */
    INKWELL,

    /**
     * Represents a manuscript object resource.
     */
    MANUSCRIPT,

    /**
     * Represents a quill object resource.
     */
    QUILL;



    /**
     * Checks if the resource is a type of reign (animal, fungi, insect, plant).
     *
     * @return true if the resource is a reign type, false otherwise.
     */
    public boolean isReign() {
        return (this.equals(ANIMAL) || this.equals(FUNGI) || this.equals(INSECT) || this.equals(PLANT));
    }

    /**
     * Checks if the resource is a type of object (inkwell, manuscript, quill).
     *
     * @return true if the resource is an object type, false otherwise.
     */
    public boolean isObject() {
        return (this.equals(INKWELL) || this.equals(MANUSCRIPT) || this.equals(QUILL));
    }


    /**
     * Provides a custom string representation of the resource with ANSI escape codes for color highlighting.
     *
     * @return A string representation of the resource with color highlighting.
     */
    public String toString(){
        String red = "\033[38;2;233;73;23m";   // Red
        String green = "\033[38;2;113;192;124m"; // green
        String blue = "\033[38;2;107;189;192m";  // Blue
        String magenta = "\033[38;2;171;63;148m";  // Magenta
        String gold = "\033[38;2;255;215;0m";  // gold
        String reset = "\u001b[0m";

        switch (this){
            case FUNGI -> {
                return red+"F"+reset;
            }
            case ANIMAL -> {
                return blue+"A"+reset;
            }
            case INSECT -> {
                return magenta+"I"+reset;
            }
            case PLANT -> {
                return green+"P"+reset;
            }
            case NULL -> {
                return reset+"X";
            }
            case EMPTY -> {
                return " ";
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
        }
        return null;
    }

    public String uncoloredToString(){
        switch (this){
            case FUNGI -> {
                return "FUNGI";
            }
            case ANIMAL -> {
                return "ANIMAL";
            }
            case INSECT -> {
                return "INSECT";
            }
            case PLANT -> {
                return "PLANT";
            }
            case NULL -> {
                return "X";
            }
            case EMPTY -> {
                return " ";
            }
            case INKWELL -> {
                return "INKWELL";
            }
            case QUILL -> {
                return "QUILL";
            }
            case MANUSCRIPT -> {
                return "MANUSCRIPT";
            }
        }
        return null;
    }
}

