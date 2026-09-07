package legion.test;

import java.util.LinkedHashMap;
import java.util.Map;
import legion.battlefield.Orientation;
import legion.errors.types.BattlefieldSizeException;
import legion.setup.LaunchParameters;
import legion.setup.ParameterValidator;
import legion.sorting.SortDirection;
import legion.sorting.SortingAlgorithm;
import legion.troops.TroopType;

/**
 * Checks for the semantic validator.
 */
public class ValidatorTests {

    private final ParameterValidator validator = new ParameterValidator();

    /**
     * Registers the validator checks in the shared report.
     *
     * @param report accumulator of results
     */
    public void register(TestReport report) {
        report.check("validator accepts a configuration that fits", () -> {
            validator.validate(parameters(6, 1, 1, 2));
            return true;
        });
        report.expectFailure("validator rejects a field smaller than five",
                BattlefieldSizeException.class, () -> validator.validate(parameters(4, 1, 1, 1)));
        report.expectFailure("validator rejects a field larger than one thousand",
                BattlefieldSizeException.class, () -> validator.validate(parameters(1001, 1, 1, 1)));
        report.expectFailure("validator rejects more troops than cells",
                BattlefieldSizeException.class, () -> validator.validate(parameters(5, 20, 20, 20)));
        report.expectFailure("validator rejects a group wider than a line",
                BattlefieldSizeException.class, () -> validator.validate(parameters(6, 1, 1, 13)));
        report.expectFailure("validator rejects more groups than lines",
                BattlefieldSizeException.class, () -> validator.validate(parameters(5, 1, 1, 1, 1, 1, 1)));
    }

    private LaunchParameters parameters(int fieldSize, int... counts) {
        Map<TroopType, Integer> map = new LinkedHashMap<>();
        TroopType[] types = TroopType.deploymentOrder();
        for (int index = 0; index < types.length; index++) {
            map.put(types[index], index < counts.length ? counts[index] : 0);
        }
        return new LaunchParameters(SortingAlgorithm.BUBBLE, SortDirection.ASCENDING,
                Orientation.SOUTH, map, fieldSize);
    }
}
