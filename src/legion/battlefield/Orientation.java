package legion.battlefield;

import legion.errors.types.InvalidParameterException;

/**
 * Side of the battlefield where the final formation is built.
 * The orientation decides whether the sorted groups are stacked in
 * rows or in columns and from which edge they grow.
 */
public enum Orientation {

    NORTH("n", "north", false, false),
    SOUTH("s", "south", false, true),
    WEST("w", "west", true, false),
    EAST("e", "east", true, true);

    private final String key;
    private final String label;
    private final boolean vertical;
    private final boolean reversed;

    Orientation(String key, String label, boolean vertical, boolean reversed) {
        this.key = key;
        this.label = label;
        this.vertical = vertical;
        this.reversed = reversed;
    }

    /**
     * Returns the command line key of the orientation.
     *
     * @return the key expected in the parameter o
     */
    public String getKey() {
        return key;
    }

    /**
     * Returns the readable name of the orientation.
     *
     * @return the label shown in the console
     */
    public String getLabel() {
        return label;
    }

    /**
     * Indicates whether the groups are stacked as columns.
     *
     * @return true when the formation grows column by column
     */
    public boolean isVertical() {
        return vertical;
    }

    /**
     * Indicates whether the formation starts on the opposite edge.
     *
     * @return true when the first group takes the last line
     */
    public boolean isReversed() {
        return reversed;
    }

    /**
     * Resolves the orientation that matches a command line key.
     *
     * @param key value received in the parameter o
     * @return the matching orientation
     * @throws InvalidParameterException when no orientation matches the key
     */
    public static Orientation fromKey(String key) {
        for (Orientation orientation : values()) {
            if (orientation.key.equalsIgnoreCase(key)) {
                return orientation;
            }
        }
        throw new InvalidParameterException("Unknown orientation: " + key + ". Expected n, s, e or w.");
    }
}
