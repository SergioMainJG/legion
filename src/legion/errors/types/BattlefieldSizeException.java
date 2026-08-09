package legion.errors.types;

import legion.errors.LegionException;

/**
 * Raised when the battlefield cannot hold the requested amount of
 * troops or when the requested field size is out of range.
 */
public class BattlefieldSizeException extends LegionException {

    private static final String CODE = "E-FIELD";

    /**
     * Creates the exception with a specific explanation.
     *
     * @param message description of the size violation
     */
    public BattlefieldSizeException(String message) {
        super(CODE, message);
    }
}
