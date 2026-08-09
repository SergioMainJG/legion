package legion.sorting.strategies;

import java.util.ArrayList;
import java.util.List;
import legion.sorting.SortingStrategy;
import legion.troops.Troop;

/**
 * Insertion sort implementation over the health of the units.
 * Efficient for the small lists handled in this milestone.
 */
public class InsertionSortStrategy implements SortingStrategy {

    private static final String NAME = "Insertion Sort";

    @Override
    public List<Troop> sort(List<Troop> troops) {
        List<Troop> ordered = new ArrayList<>(troops);
        for (int index = 1; index < ordered.size(); index++) {
            insertAtItsPlace(ordered, index);
        }
        return ordered;
    }

    private void insertAtItsPlace(List<Troop> ordered, int index) {
        Troop candidate = ordered.get(index);
        int position = index - 1;
        while (position >= 0 && ordered.get(position).getHealth() > candidate.getHealth()) {
            ordered.set(position + 1, ordered.get(position));
            position--;
        }
        ordered.set(position + 1, candidate);
    }

    @Override
    public String getName() {
        return NAME;
    }
}
