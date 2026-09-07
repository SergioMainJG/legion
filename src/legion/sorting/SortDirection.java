package legion.sorting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import legion.errors.types.InvalidParameterException;
import legion.troops.Troop;

/**
 * Direction applied to the result of a sorting strategy.
 * The command line parameter {@code t} selects this direction:
 * {@code c} for ascending and {@code d} for descending. This is the
 * interpretation adopted for the final delivery and it matches the
 * behaviour approved in the midterm. The strategies always produce an
 * ascending list, so the descending case is resolved by reversing the
 * result once instead of running a second algorithm.
 */
public enum SortDirection {

    ASCENDING("c", "ascending"),
    DESCENDING("d", "descending");

    private final String key;
    private final String label;

    SortDirection(String key, String label) {
        this.key = key;
        this.label = label;
    }

    /**
     * Returns the command line key of the direction.
     *
     * @return the key expected in the parameter t
     */
    public String getKey() {
        return key;
    }

    /**
     * Returns the readable name of the direction.
     *
     * @return the label shown in the console
     */
    public String getLabel() {
        return label;
    }

    /**
     * Applies the direction to an already ascending list.
     *
     * @param ascending list produced by a sorting strategy
     * @return a new list ordered according to this direction
     */
    public List<Troop> apply(List<Troop> ascending) {
        List<Troop> result = new ArrayList<>(ascending);
        if (this == DESCENDING) {
            Collections.reverse(result);
        }
        return result;
    }

    /**
     * Resolves the direction that matches a command line key.
     *
     * @param key value received in the parameter t
     * @return the matching direction
     * @throws InvalidParameterException when no direction matches the key
     */
    public static SortDirection fromKey(String key) {
        for (SortDirection direction : values()) {
            if (direction.key.equalsIgnoreCase(key)) {
                return direction;
            }
        }
        throw new InvalidParameterException("Unknown sort direction: " + key + ". Expected c or d.");
    }
}
