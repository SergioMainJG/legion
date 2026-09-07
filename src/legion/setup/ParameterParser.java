package legion.setup;

import java.util.LinkedHashMap;
import java.util.Map;
import legion.battlefield.Battlefield;
import legion.battlefield.Orientation;
import legion.errors.types.InvalidParameterException;
import legion.sorting.SortDirection;
import legion.sorting.SortingAlgorithm;
import legion.troops.TroopType;

/**
 * Translation of the command line arguments into a configuration.
 * The parser only checks the shape of the pairs and the format of each
 * value, the rules that relate several values belong to the validator.
 */
public class ParameterParser {

    private static final String PAIR_SEPARATOR = "=";
    private static final String COUNT_SEPARATOR = ",";
    private static final String ALGORITHM_KEY = "a";
    private static final String DIRECTION_KEY = "t";
    private static final String ORIENTATION_KEY = "o";
    private static final String UNITS_KEY = "u";
    private static final String FIELD_KEY = "f";
    private static final String NUMBER_PATTERN = "\\d+";
    private static final int PAIR_PARTS = 2;

    /**
     * Parses the arguments received by the entry point.
     *
     * @param arguments raw command line arguments
     * @return the configuration described by the arguments
     * @throws InvalidParameterException when a pair is malformed, duplicated or missing
     */
    public LaunchParameters parse(String[] arguments) {
        Map<String, String> pairs = readPairs(arguments);
        return new LaunchParameters(
                SortingAlgorithm.fromKey(require(pairs, ALGORITHM_KEY)),
                SortDirection.fromKey(require(pairs, DIRECTION_KEY)),
                Orientation.fromKey(require(pairs, ORIENTATION_KEY)),
                parseCounts(require(pairs, UNITS_KEY)),
                parseFieldSize(pairs.get(FIELD_KEY)));
    }

    private Map<String, String> readPairs(String[] arguments) {
        Map<String, String> pairs = new LinkedHashMap<>();
        for (String argument : arguments) {
            String[] parts = argument.split(PAIR_SEPARATOR, -1);
            if (parts.length != PAIR_PARTS || parts[0].isBlank() || parts[1].isBlank()) {
                throw new InvalidParameterException("Malformed parameter: " + argument + ". Expected key=value.");
            }
            String key = parts[0].trim().toLowerCase();
            if (pairs.containsKey(key)) {
                throw new InvalidParameterException("Duplicated parameter: " + key
                        + ". Each parameter must appear once.");
            }
            pairs.put(key, parts[1].trim());
        }
        return pairs;
    }

    private String require(Map<String, String> pairs, String key) {
        String value = pairs.get(key);
        if (value == null) {
            throw new InvalidParameterException("Missing required parameter: " + key + ".");
        }
        return value;
    }

    private Map<TroopType, Integer> parseCounts(String value) {
        String[] parts = value.split(COUNT_SEPARATOR, -1);
        TroopType[] types = TroopType.deploymentOrder();
        if (parts.length < 1 || parts.length > types.length) {
            throw new InvalidParameterException("Parameter u expects between 1 and " + types.length
                    + " values separated by commas, received " + parts.length + ".");
        }
        Map<TroopType, Integer> counts = new LinkedHashMap<>();
        for (int index = 0; index < types.length; index++) {
            int amount = index < parts.length ? parseCount(types[index], parts[index].trim()) : 0;
            counts.put(types[index], amount);
        }
        return counts;
    }

    private int parseCount(TroopType type, String value) {
        if (!value.matches(NUMBER_PATTERN)) {
            throw new InvalidParameterException("Amount of " + type.getLabel()
                    + " must be a whole number, received " + value + ".");
        }
        return Integer.parseInt(value);
    }

    private int parseFieldSize(String value) {
        if (value == null) {
            return Battlefield.DEFAULT_SIZE;
        }
        if (!value.matches(NUMBER_PATTERN)) {
            throw new InvalidParameterException("Parameter f must be a whole number, received " + value + ".");
        }
        return Integer.parseInt(value);
    }
}
