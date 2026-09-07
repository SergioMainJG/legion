package legion.sorting.strategies;

import java.util.ArrayList;
import java.util.List;
import legion.sorting.SortingStrategy;
import legion.sorting.TroopComparator;
import legion.troops.Troop;

/**
 * Selection sort over the shared range comparator.
 * It repeatedly moves the smallest remaining unit to the front.
 */
public class SelectionSortStrategy implements SortingStrategy {

    private static final String NAME = "Selection Sort";

    @Override
    public List<Troop> sort(List<Troop> troops) {
        List<Troop> ordered = new ArrayList<>(troops);
        for (int index = 0; index < ordered.size() - 1; index++) {
            swap(ordered, index, smallestFrom(ordered, index));
        }
        return ordered;
    }

    private int smallestFrom(List<Troop> ordered, int start) {
        int smallest = start;
        for (int index = start + 1; index < ordered.size(); index++) {
            if (TroopComparator.BY_RANGE.compare(ordered.get(index), ordered.get(smallest)) < 0) {
                smallest = index;
            }
        }
        return smallest;
    }

    private void swap(List<Troop> ordered, int first, int second) {
        Troop temporary = ordered.get(first);
        ordered.set(first, ordered.get(second));
        ordered.set(second, temporary);
    }

    @Override
    public String getName() {
        return NAME;
    }
}
