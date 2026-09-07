package legion.test;

import java.util.function.BooleanSupplier;

/**
 * Accumulator of test results shared by every test group.
 * It prints one line per check and a final summary.
 */
public class TestReport {

    private static final String PASS = "[PASS] ";
    private static final String FAIL = "[FAIL] ";

    private int passed;
    private int failed;

    /**
     * Runs a boolean check and records its result.
     *
     * @param name  description of the expectation
     * @param check supplier that returns true when the expectation holds
     */
    public void check(String name, BooleanSupplier check) {
        boolean result;
        try {
            result = check.getAsBoolean();
        } catch (RuntimeException failure) {
            System.out.println(FAIL + name + " -> threw " + failure);
            failed++;
            return;
        }
        if (result) {
            System.out.println(PASS + name);
            passed++;
        } else {
            System.out.println(FAIL + name);
            failed++;
        }
    }

    /**
     * Records a check that expects a runnable to raise a given exception.
     *
     * @param name     description of the expectation
     * @param expected exception type that must be raised
     * @param action   code that must fail
     */
    public void expectFailure(String name, Class<? extends RuntimeException> expected, Runnable action) {
        try {
            action.run();
            System.out.println(FAIL + name + " -> no exception was raised");
            failed++;
        } catch (RuntimeException failure) {
            if (expected.isInstance(failure)) {
                System.out.println(PASS + name);
                passed++;
            } else {
                System.out.println(FAIL + name + " -> raised " + failure);
                failed++;
            }
        }
    }

    /**
     * Prints the aggregated totals.
     */
    public void summarise() {
        System.out.println("--------------------------------------------------");
        System.out.println("Total: " + (passed + failed) + "  Passed: " + passed + "  Failed: " + failed);
    }

    /**
     * Indicates whether at least one check failed.
     *
     * @return true when there is a failure
     */
    public boolean hasFailures() {
        return failed > 0;
    }
}
