package legion.errors.types;

import legion.errors.LegionException;

/**
 * Raised when the interactive loop receives a command that is not
 * registered or is written with the wrong amount of arguments.
 */
public class InvalidCommandException extends LegionException {

    private static final String CODE = "E-CMD";

    /**
     * Creates the exception for an invalid command line.
     *
     * @param message description of the command problem
     */
    public InvalidCommandException(String message) {
        super(CODE, message);
    }
}
