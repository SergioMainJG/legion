package legion.sorting.strategies;

import java.util.ArrayList;
import java.util.List;
import legion.sorting.SortingStrategy;
import legion.sorting.TroopComparator;
import legion.troops.Troop;

/**
 * Merge sort over the shared range comparator.
 * It splits the list in halves and merges them back in order.
 */
public class MergeSortStrategy implements SortingStrategy {

    private static final String NAME = "Merge Sort";

    @Override
    public List<Troop> sort(List<Troop> troops) {
        List<Troop> ordered = new ArrayList<>(troops);
        if (ordered.size() < 2) {
            return ordered;
        }
        return mergeSort(ordered);
    }

    private List<Troop> mergeSort(List<Troop> ordered) {
        if (ordered.size() < 2) {
            return ordered;
        }
        int middle = ordered.size() / 2;
        List<Troop> left = mergeSort(new ArrayList<>(ordered.subList(0, middle)));
        List<Troop> right = mergeSort(new ArrayList<>(ordered.subList(middle, ordered.size())));
        return merge(left, right);
    }

    private List<Troop> merge(List<Troop> left, List<Troop> right) {
        List<Troop> merged = new ArrayList<>(left.size() + right.size());
        int leftIndex = 0;
        int rightIndex = 0;
        while (leftIndex < left.size() && rightIndex < right.size()) {
            if (TroopComparator.BY_RANGE.compare(left.get(leftIndex), right.get(rightIndex)) <= 0) {
                merged.add(left.get(leftIndex));
                leftIndex++;
            } else {
                merged.add(right.get(rightIndex));
                rightIndex++;
            }
        }
        merged.addAll(left.subList(leftIndex, left.size()));
        merged.addAll(right.subList(rightIndex, right.size()));
        return merged;
    }

    @Override
    public String getName() {
        return NAME;
    }
}
