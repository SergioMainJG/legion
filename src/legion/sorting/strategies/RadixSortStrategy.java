package legion.sorting.strategies;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import legion.sorting.SortingStrategy;
import legion.troops.Troop;

/**
 * Least significant digit radix sort over the attack range.
 * Each pass is a stable counting sort on one decimal digit. The input
 * is pre ordered by identifier so ties follow the same rule as the
 * comparison strategies.
 */
public class RadixSortStrategy implements SortingStrategy {

    private static final String NAME = "Radix Sort";
    private static final int RADIX = 10;

    @Override
    public List<Troop> sort(List<Troop> troops) {
        List<Troop> ordered = new ArrayList<>(troops);
        ordered.sort(Comparator.comparing(Troop::getIdentifier));
        if (ordered.isEmpty()) {
            return ordered;
        }
        int maximumRange = ordered.stream().mapToInt(Troop::getRange).max().orElse(0);
        for (int exponent = 1; maximumRange / exponent > 0; exponent *= RADIX) {
            ordered = countingPass(ordered, exponent);
        }
        return ordered;
    }

    private List<Troop> countingPass(List<Troop> input, int exponent) {
        int[] counts = new int[RADIX];
        for (Troop troop : input) {
            counts[digitOf(troop, exponent)]++;
        }
        for (int digit = 1; digit < RADIX; digit++) {
            counts[digit] += counts[digit - 1];
        }
        Troop[] output = new Troop[input.size()];
        for (int index = input.size() - 1; index >= 0; index--) {
            Troop troop = input.get(index);
            int digit = digitOf(troop, exponent);
            counts[digit]--;
            output[counts[digit]] = troop;
        }
        return new ArrayList<>(List.of(output));
    }

    private int digitOf(Troop troop, int exponent) {
        return troop.getRange() / exponent % RADIX;
    }

    @Override
    public String getName() {
        return NAME;
    }
}
