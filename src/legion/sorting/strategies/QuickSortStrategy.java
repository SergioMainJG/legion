package legion.sorting.strategies;

import java.util.ArrayList;
import java.util.List;
import legion.sorting.SortingStrategy;
import legion.sorting.TroopComparator;
import legion.troops.Troop;

/**
 * Quick sort over the shared range comparator.
 * It partitions the list around the last element of each range.
 */
public class QuickSortStrategy implements SortingStrategy {

    private static final String NAME = "Quick Sort";

    @Override
    public List<Troop> sort(List<Troop> troops) {
        List<Troop> ordered = new ArrayList<>(troops);
        quickSort(ordered, 0, ordered.size() - 1);
        return ordered;
    }

    private void quickSort(List<Troop> ordered, int low, int high) {
        if (low >= high) {
            return;
        }
        int pivotIndex = partition(ordered, low, high);
        quickSort(ordered, low, pivotIndex - 1);
        quickSort(ordered, pivotIndex + 1, high);
    }

    private int partition(List<Troop> ordered, int low, int high) {
        Troop pivot = ordered.get(high);
        int boundary = low;
        for (int index = low; index < high; index++) {
            if (TroopComparator.BY_RANGE.compare(ordered.get(index), pivot) < 0) {
                swap(ordered, boundary, index);
                boundary++;
            }
        }
        swap(ordered, boundary, high);
        return boundary;
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
