package legion.errors;

/**
 * Root of the exception hierarchy of the application.
 * Every recoverable domain failure extends this type so a single
 * handler can process all of them uniformly.
 */
public abstract class LegionException extends RuntimeException {

    private final String code;

    /**
     * Creates a domain exception.
     *
     * @param code    short identifier shown to the user
     * @param message human readable description of the failure
     */
    protected LegionException(String code, String message) {
        super(message);
        this.code = code;
    }

    /**
     * Returns the short identifier of this failure.
     *
     * @return the exception code
     */
    public String getCode() {
        return code;
    }
}
