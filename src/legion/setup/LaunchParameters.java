package legion.setup;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import legion.battlefield.Orientation;
import legion.sorting.SortDirection;
import legion.sorting.SortingAlgorithm;
import legion.troops.TroopType;

/**
 * Immutable configuration of a run of the simulator.
 * It is produced by the parser, checked by the validator and consumed
 * by the application without further changes.
 */
public class LaunchParameters {

    private final SortingAlgorithm algorithm;
    private final SortDirection direction;
    private final Orientation orientation;
    private final Map<TroopType, Integer> troopCounts;
    private final int fieldSize;

    /**
     * Creates the configuration of a run.
     *
     * @param algorithm   sorting strategy selected by the user
     * @param direction   ascending or descending order
     * @param orientation edge where the final formation is built
     * @param troopCounts amount of units requested per type
     * @param fieldSize   amount of rows and columns of the matrix
     */
    public LaunchParameters(SortingAlgorithm algorithm, SortDirection direction, Orientation orientation,
                            Map<TroopType, Integer> troopCounts, int fieldSize) {
        this.algorithm = algorithm;
        this.direction = direction;
        this.orientation = orientation;
        this.troopCounts = Collections.unmodifiableMap(new LinkedHashMap<>(troopCounts));
        this.fieldSize = fieldSize;
    }

    /**
     * Returns the selected sorting algorithm.
     *
     * @return the catalogue entry chosen by the user
     */
    public SortingAlgorithm getAlgorithm() {
        return algorithm;
    }

    /**
     * Returns the selected order direction.
     *
     * @return ascending or descending
     */
    public SortDirection getDirection() {
        return direction;
    }

    /**
     * Returns the selected formation orientation.
     *
     * @return the edge where the formation is built
     */
    public Orientation getOrientation() {
        return orientation;
    }

    /**
     * Returns the requested amount of units per type.
     *
     * @return an unmodifiable map of counts
     */
    public Map<TroopType, Integer> getTroopCounts() {
        return troopCounts;
    }

    /**
     * Returns the total amount of requested units.
     *
     * @return the sum of every count
     */
    public int getTotalTroops() {
        return troopCounts.values().stream().mapToInt(Integer::intValue).sum();
    }

    /**
     * Returns the size of the battlefield.
     *
     * @return the amount of rows and columns
     */
    public int getFieldSize() {
        return fieldSize;
    }
}
