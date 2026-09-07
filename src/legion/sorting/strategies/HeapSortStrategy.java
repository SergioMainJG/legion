package legion.sorting.strategies;

import java.util.ArrayList;
import java.util.List;
import legion.sorting.SortingStrategy;
import legion.sorting.TroopComparator;
import legion.troops.Troop;

/**
 * Heap sort over the shared range comparator.
 * It builds a max heap and extracts the largest unit repeatedly.
 */
public class HeapSortStrategy implements SortingStrategy {

    private static final String NAME = "Heap Sort";

    @Override
    public List<Troop> sort(List<Troop> troops) {
        List<Troop> ordered = new ArrayList<>(troops);
        int size = ordered.size();
        for (int root = size / 2 - 1; root >= 0; root--) {
            siftDown(ordered, root, size);
        }
        for (int end = size - 1; end > 0; end--) {
            swap(ordered, 0, end);
            siftDown(ordered, 0, end);
        }
        return ordered;
    }

    private void siftDown(List<Troop> ordered, int root, int size) {
        int largest = root;
        int left = 2 * root + 1;
        int right = 2 * root + 2;
        if (left < size && TroopComparator.BY_RANGE.compare(ordered.get(left), ordered.get(largest)) > 0) {
            largest = left;
        }
        if (right < size && TroopComparator.BY_RANGE.compare(ordered.get(right), ordered.get(largest)) > 0) {
            largest = right;
        }
        if (largest != root) {
            swap(ordered, root, largest);
            siftDown(ordered, largest, size);
        }
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
