package legion.errors.types;

import legion.errors.LegionException;

/**
 * Raised when the requested sorting algorithm key does not exist
 * or is not implemented in the current milestone.
 */
public class InvalidAlgorithmException extends LegionException {

    private static final String CODE = "E-ALG";
    private static final String UNKNOWN_PREFIX = "Unknown sorting algorithm: ";
    private static final String PENDING_PREFIX = "Sorting algorithm ";
    private static final String PENDING_SUFFIX = " is planned for the second milestone. Available now: ";

    /**
     * Creates the exception for an unknown algorithm key.
     *
     * @param key the key received from the user
     */
    public InvalidAlgorithmException(String key) {
        super(CODE, UNKNOWN_PREFIX + key);
    }

    private InvalidAlgorithmException(String key, String available) {
        super(CODE, PENDING_PREFIX + key + PENDING_SUFFIX + available);
    }

    /**
     * Creates the exception for an algorithm that exists but is a stub.
     *
     * @param key       the key received from the user
     * @param available keys of the algorithms that already work
     * @return the exception describing the pending implementation
     */
    public static InvalidAlgorithmException notImplemented(String key, String available) {
        return new InvalidAlgorithmException(key, available);
    }
}
