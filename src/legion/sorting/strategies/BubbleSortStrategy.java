package legion.sorting.strategies;

import java.util.ArrayList;
import java.util.List;
import legion.sorting.SortingStrategy;
import legion.troops.Troop;

/**
 * Bubble sort implementation over the health of the units.
 * Stable and simple, kept as the reference implementation of the
 * milestone.
 */
public class BubbleSortStrategy implements SortingStrategy {

    private static final String NAME = "Bubble Sort";

    @Override
    public List<Troop> sort(List<Troop> troops) {
        List<Troop> ordered = new ArrayList<>(troops);
        for (int pass = 0; pass < ordered.size() - 1; pass++) {
            if (!bubblePass(ordered, pass)) {
                return ordered;
            }
        }
        return ordered;
    }

    private boolean bubblePass(List<Troop> ordered, int pass) {
        boolean swapped = false;
        for (int index = 0; index < ordered.size() - 1 - pass; index++) {
            if (ordered.get(index).getHealth() > ordered.get(index + 1).getHealth()) {
                swap(ordered, index, index + 1);
                swapped = true;
            }
        }
        return swapped;
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
