package legion.setup;

import java.util.Map;
import legion.errors.types.BattlefieldSizeException;
import legion.errors.types.InvalidAlgorithmException;
import legion.errors.types.InvalidParameterException;
import legion.sorting.SortingAlgorithm;
import legion.troops.TroopType;

/**
 * Semantic rules applied to a configuration before the run starts.
 * Format problems are already rejected by the parser, this class only
 * checks that the values make sense together.
 */
public class ParameterValidator {

    private static final int MINIMUM_FIELD_SIZE = 2;
    private static final int MAXIMUM_FIELD_SIZE = 1000;
    private static final int MINIMUM_TROOPS = 1;

    /**
     * Validates a configuration produced by the parser or the menu.
     *
     * @param parameters configuration to validate
     * @throws InvalidAlgorithmException  when the algorithm is only a placeholder
     * @throws BattlefieldSizeException   when the field cannot hold the units
     * @throws InvalidParameterException  when a count is not usable
     */
    public void validate(LaunchParameters parameters) {
        validateAlgorithm(parameters.getAlgorithm());
        validateFieldSize(parameters.getFieldSize());
        validateCounts(parameters.getTroopCounts());
        validateCapacity(parameters);
    }

    private void validateAlgorithm(SortingAlgorithm algorithm) {
        if (!algorithm.isImplemented()) {
            throw InvalidAlgorithmException.notImplemented(algorithm.getKey(),
                    SortingAlgorithm.implementedKeys());
        }
    }

    private void validateFieldSize(int fieldSize) {
        if (fieldSize < MINIMUM_FIELD_SIZE || fieldSize > MAXIMUM_FIELD_SIZE) {
            throw new BattlefieldSizeException("Field size must be between " + MINIMUM_FIELD_SIZE
                    + " and " + MAXIMUM_FIELD_SIZE + ", received " + fieldSize + ".");
        }
    }

    private void validateCounts(Map<TroopType, Integer> counts) {
        int total = counts.values().stream().mapToInt(Integer::intValue).sum();
        if (total < MINIMUM_TROOPS) {
            throw new InvalidParameterException("At least " + MINIMUM_TROOPS + " troop must be deployed.");
        }
    }

    private void validateCapacity(LaunchParameters parameters) {
        int capacity = parameters.getFieldSize() * parameters.getFieldSize();
        if (parameters.getTotalTroops() > capacity) {
            throw new BattlefieldSizeException("The battlefield holds " + capacity
                    + " cells and " + parameters.getTotalTroops() + " troops were requested.");
        }
        validateFormationCapacity(parameters);
    }

    private void validateFormationCapacity(LaunchParameters parameters) {
        int lines = parameters.getFieldSize();
        int groups = (int) parameters.getTroopCounts().values().stream().filter(count -> count > 0).count();
        if (groups > lines) {
            throw new BattlefieldSizeException("The final formation needs " + groups
                    + " lines and the battlefield only has " + lines + ".");
        }
        for (Map.Entry<TroopType, Integer> entry : parameters.getTroopCounts().entrySet()) {
            if (entry.getValue() > lines) {
                throw new BattlefieldSizeException("A line holds " + lines + " units and "
                        + entry.getValue() + " " + entry.getKey().getLabel() + " were requested.");
            }
        }
    }
}
