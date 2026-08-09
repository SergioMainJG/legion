package legion.battlefield;

/**
 * Immutable coordinate inside the battlefield matrix.
 * The origin is the upper left corner, x grows to the right and y
 * grows downwards.
 *
 * @param x horizontal coordinate
 * @param y vertical coordinate
 */
public record Position(int x, int y) {

    private static final String OPENING = "(";
    private static final String CLOSING = ")";
    private static final String SEPARATOR = ", ";

    /**
     * Builds a position shifted by the given amount of cells.
     *
     * @param deltaX horizontal displacement
     * @param deltaY vertical displacement
     * @return a new position, the original one is never modified
     */
    public Position shift(int deltaX, int deltaY) {
        return new Position(x + deltaX, y + deltaY);
    }

    /**
     * Returns the human readable form of the coordinate.
     *
     * @return the coordinate as a parenthesised pair
     */
    @Override
    public String toString() {
        return OPENING + x + SEPARATOR + y + CLOSING;
    }
}
