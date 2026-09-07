package legion.test;

/**
 * Minimal reproducible test harness for the simulator.
 * The project forbids build tools, so the checks are plain Java methods
 * gathered here and executed from a single entry point.
 */
public final class TestRunner {

    private TestRunner() {
    }

    /**
     * Runs every test group and reports the aggregated result.
     *
     * @param arguments ignored
     */
    public static void main(String[] arguments) {
        TestReport report = new TestReport();
        new ParserTests().register(report);
        new ValidatorTests().register(report);
        new SortingTests().register(report);
        new BattlefieldTests().register(report);
        new FormationTests().register(report);
        report.summarise();
        if (report.hasFailures()) {
            System.exit(1);
        }
    }
}
