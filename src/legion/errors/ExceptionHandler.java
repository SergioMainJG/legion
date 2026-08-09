package legion.errors;

import legion.console.ConsoleWriter;

/**
 * Centralized translation of exceptions into user facing output.
 * The application intercepts failures only in the entry point and in
 * the interactive loop, and both delegate here.
 */
public class ExceptionHandler {

    private static final String UNEXPECTED_CODE = "E-UNEXPECTED";
    private static final String UNEXPECTED_MESSAGE = "Unexpected failure. The operation was cancelled.";

    private final ConsoleWriter console;

    /**
     * Creates the handler with the output channel it must use.
     *
     * @param console writer used to report the failure
     */
    public ExceptionHandler(ConsoleWriter console) {
        this.console = console;
    }

    /**
     * Reports a failure using the uniform error format.
     *
     * @param failure exception captured by the caller
     */
    public void handle(Throwable failure) {
        if (failure instanceof LegionException domainFailure) {
            console.writeFailure(domainFailure.getCode(), domainFailure.getMessage());
            return;
        }
        console.writeFailure(UNEXPECTED_CODE, describeUnexpected(failure));
    }

    private String describeUnexpected(Throwable failure) {
        if (failure.getMessage() == null) {
            return UNEXPECTED_MESSAGE;
        }
        return UNEXPECTED_MESSAGE + " Detail: " + failure.getMessage();
    }
}
