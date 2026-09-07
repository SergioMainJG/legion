package legion;

import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import legion.battlefield.Battlefield;
import legion.battlefield.BattlefieldRenderer;
import legion.battlefield.FormationArranger;
import legion.battlefield.RandomDeployer;
import legion.commands.GameLoop;
import legion.console.ConsoleWriter;
import legion.errors.ExceptionHandler;
import legion.setup.InteractiveParameterReader;
import legion.setup.LaunchParameters;
import legion.setup.ParameterParser;
import legion.setup.ParameterValidator;
import legion.sorting.SortingStrategy;
import legion.troops.Troop;
import legion.troops.TroopFactory;

/**
 * Orchestrator of a complete run of the simulator.
 * It resolves the configuration, deploys the legion, sorts it and hands
 * the control to the interactive session.
 */
public class LegionApplication {

    private static final String BANNER = "March of the Legion";
    private static final String INITIAL_FIELD = "Initial deployment";
    private static final String FINAL_FIELD = "Final formation";
    private static final String CONFIGURATION_TITLE = "Configuration";
    private static final String SORTING_TITLE = "Sorting report";
    private static final double NANOSECONDS_IN_MILLISECOND = 1_000_000.0;

    private final ConsoleWriter console;
    private final ExceptionHandler handler;
    private final Scanner scanner;
    private final ParameterParser parser = new ParameterParser();
    private final ParameterValidator validator = new ParameterValidator();
    private final RandomDeployer deployer = new RandomDeployer(new TroopFactory());
    private final FormationArranger arranger = new FormationArranger();

    /**
     * Creates the orchestrator with the shared collaborators.
     *
     * @param console output channel of the run
     * @param handler centralized reporter of failures
     * @param scanner input source of the run
     */
    public LegionApplication(ConsoleWriter console, ExceptionHandler handler, Scanner scanner) {
        this.console = console;
        this.handler = handler;
        this.scanner = scanner;
    }

    /**
     * Executes a full run of the simulator.
     *
     * @param arguments raw command line arguments
     */
    public void run(String[] arguments) {
        console.writeTitle(BANNER);
        LaunchParameters parameters = resolveParameters(arguments);
        validator.validate(parameters);
        reportConfiguration(parameters);
        Battlefield battlefield = new Battlefield(parameters.getFieldSize());
        BattlefieldRenderer renderer = new BattlefieldRenderer(console);
        List<Troop> troops = deployer.deploy(battlefield, parameters.getTroopCounts());
        renderer.render(battlefield, INITIAL_FIELD);
        List<Troop> sorted = sortTroops(parameters, troops);
        arranger.arrange(battlefield, sorted, parameters.getOrientation());
        renderer.render(battlefield, FINAL_FIELD);
        new GameLoop(console, renderer, handler, scanner, battlefield).run();
    }

    private LaunchParameters resolveParameters(String[] arguments) {
        if (arguments.length == 0) {
            return new InteractiveParameterReader(console, scanner).read();
        }
        return parser.parse(arguments);
    }

    private void reportConfiguration(LaunchParameters parameters) {
        console.writeTitle(CONFIGURATION_TITLE);
        console.writeLabeled("Algorithm", parameters.getAlgorithm().createStrategy().getName());
        console.writeLabeled("Order", parameters.getDirection().getLabel());
        console.writeLabeled("Orientation", parameters.getOrientation().getLabel());
        console.writeLabeled("Field", parameters.getFieldSize() + "x" + parameters.getFieldSize());
        console.writeLabeled("Troops", String.valueOf(parameters.getTotalTroops()));
        console.writeHeavySeparator();
    }

    private List<Troop> sortTroops(LaunchParameters parameters, List<Troop> troops) {
        SortingStrategy strategy = parameters.getAlgorithm().createStrategy();
        long start = System.nanoTime();
        List<Troop> ascending = strategy.sort(troops);
        long elapsed = System.nanoTime() - start;
        List<Troop> sorted = parameters.getDirection().apply(ascending);
        reportSorting(strategy, parameters, sorted, elapsed);
        return sorted;
    }

    private void reportSorting(SortingStrategy strategy, LaunchParameters parameters,
                               List<Troop> sorted, long elapsed) {
        console.writeTitle(SORTING_TITLE);
        console.writeLabeled("Strategy", strategy.getName());
        console.writeLabeled("Criterion", "attack range");
        console.writeLabeled("Direction", parameters.getDirection().getLabel());
        console.writeLabeled("Sorting time", formatMilliseconds(elapsed) + " ms (" + elapsed + " ns)");
        console.writeLabeled("Result", sorted.toString());
        console.writeHeavySeparator();
    }

    private String formatMilliseconds(long elapsed) {
        return String.format(Locale.ROOT, "%.6f", elapsed / NANOSECONDS_IN_MILLISECOND);
    }
}
