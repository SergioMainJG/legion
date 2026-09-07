package legion.errors.types;

import legion.errors.LegionException;

/**
 * Raised when the requested sorting algorithm key does not exist in
 * the catalogue.
 */
public class InvalidAlgorithmException extends LegionException {

    private static final String CODE = "E-ALG";
    private static final String UNKNOWN_PREFIX = "Unknown sorting algorithm: ";

    /**
     * Creates the exception for an unknown algorithm key.
     *
     * @param key the key received from the user
     */
    public InvalidAlgorithmException(String key) {
        super(CODE, UNKNOWN_PREFIX + key);
    }
}
