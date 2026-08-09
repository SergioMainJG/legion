package legion.sorting.strategies;

import java.util.List;
import legion.sorting.SortingStrategy;
import legion.troops.Troop;

/**
 * Placeholder of the Heap Sort algorithm.
 * The structure is already registered in the catalogue so the second
 * milestone only has to fill the body of the sorting method.
 */
public class HeapSortStrategy implements SortingStrategy {

    private static final String NAME = "Heap Sort";
    private static final String PENDING = "Heap Sort is planned for the second milestone.";

    @Override
    public List<Troop> sort(List<Troop> troops) {
        throw new UnsupportedOperationException(PENDING);
    }

    @Override
    public String getName() {
        return NAME;
    }
}
