package legion.setup;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import legion.battlefield.Orientation;
import legion.console.ConsoleWriter;
import legion.errors.types.InvalidParameterException;
import legion.sorting.SortDirection;
import legion.sorting.SortingAlgorithm;
import legion.troops.TroopType;

/**
 * Guided menu used when the program is launched without arguments.
 * It asks for the same values as the command line and produces the
 * very same configuration object.
 */
public class InteractiveParameterReader {

    private static final String TITLE = "Interactive setup";
    private static final String ALGORITHM_PROMPT = "Sorting algorithm (b=bubble, i=insertion)";
    private static final String DIRECTION_PROMPT = "Order (c=ascending, d=descending)";
    private static final String ORIENTATION_PROMPT = "Orientation (n, s, e, w)";
    private static final String FIELD_PROMPT = "Field size (empty for 6)";
    private static final String UNITS_PROMPT_PREFIX = "Amount of ";
    private static final String PROMPT_SUFFIX = ": ";
    private static final String NUMBER_PATTERN = "\\d+";
    private static final int DEFAULT_FIELD_SIZE = 6;

    private final ConsoleWriter console;
    private final Scanner scanner;

    /**
     * Creates the reader with the console and the shared scanner.
     *
     * @param console output channel used for the prompts
     * @param scanner input source of the session
     */
    public InteractiveParameterReader(ConsoleWriter console, Scanner scanner) {
        this.console = console;
        this.scanner = scanner;
    }

    /**
     * Asks the user for every configuration value.
     *
     * @return the configuration described by the answers
     * @throws InvalidParameterException when an answer is not usable
     */
    public LaunchParameters read() {
        console.writeTitle(TITLE);
        SortingAlgorithm algorithm = SortingAlgorithm.fromKey(ask(ALGORITHM_PROMPT));
        SortDirection direction = SortDirection.fromKey(ask(DIRECTION_PROMPT));
        Orientation orientation = Orientation.fromKey(ask(ORIENTATION_PROMPT));
        Map<TroopType, Integer> counts = askCounts();
        int fieldSize = askFieldSize();
        console.writeHeavySeparator();
        return new LaunchParameters(algorithm, direction, orientation, counts, fieldSize);
    }

    private Map<TroopType, Integer> askCounts() {
        Map<TroopType, Integer> counts = new LinkedHashMap<>();
        for (TroopType type : TroopType.implementedValues()) {
            counts.put(type, askNumber(UNITS_PROMPT_PREFIX + type.getLabel()));
        }
        return counts;
    }

    private int askFieldSize() {
        String answer = ask(FIELD_PROMPT);
        if (answer.isBlank()) {
            return DEFAULT_FIELD_SIZE;
        }
        return toNumber(FIELD_PROMPT, answer);
    }

    private int askNumber(String prompt) {
        return toNumber(prompt, ask(prompt));
    }

    private int toNumber(String prompt, String answer) {
        if (!answer.matches(NUMBER_PATTERN)) {
            throw new InvalidParameterException(prompt + " expects a whole number, received " + answer + ".");
        }
        return Integer.parseInt(answer);
    }

    private String ask(String prompt) {
        console.writePrompt(prompt + PROMPT_SUFFIX);
        if (!scanner.hasNextLine()) {
            throw new InvalidParameterException("Input ended before the setup was completed.");
        }
        return scanner.nextLine().trim();
    }
}
