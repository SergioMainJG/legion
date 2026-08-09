package legion.errors.types;

import legion.errors.LegionException;

/**
 * Raised when a launch parameter is missing, duplicated or has a
 * value that does not respect the expected format.
 */
public class InvalidParameterException extends LegionException {

    private static final String CODE = "E-PARAM";

    /**
     * Creates the exception for an invalid launch parameter.
     *
     * @param message description of the parameter problem
     */
    public InvalidParameterException(String message) {
        super(CODE, message);
    }
}
