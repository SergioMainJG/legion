package legion.sorting.strategies;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import legion.sorting.SortingStrategy;
import legion.troops.Troop;

/**
 * Counting sort over the attack range of the units.
 * The range is a small non negative integer, so a direct count of
 * occurrences produces the order without comparisons. The input is
 * pre ordered by identifier so ties follow the same rule as the
 * comparison strategies.
 */
public class CountingSortStrategy implements SortingStrategy {

    private static final String NAME = "Counting Sort";

    @Override
    public List<Troop> sort(List<Troop> troops) {
        List<Troop> stable = new ArrayList<>(troops);
        stable.sort(Comparator.comparing(Troop::getIdentifier));
        if (stable.isEmpty()) {
            return stable;
        }
        int maximumRange = stable.stream().mapToInt(Troop::getRange).max().orElse(0);
        int[] counts = new int[maximumRange + 1];
        for (Troop troop : stable) {
            counts[troop.getRange()]++;
        }
        for (int value = 1; value < counts.length; value++) {
            counts[value] += counts[value - 1];
        }
        Troop[] ordered = new Troop[stable.size()];
        for (int index = stable.size() - 1; index >= 0; index--) {
            Troop troop = stable.get(index);
            counts[troop.getRange()]--;
            ordered[counts[troop.getRange()]] = troop;
        }
        return new ArrayList<>(List.of(ordered));
    }

    @Override
    public String getName() {
        return NAME;
    }
}
