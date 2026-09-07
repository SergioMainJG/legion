package legion.sorting;

import java.util.Comparator;
import legion.troops.Troop;

/**
 * Central ordering criterion of the legion.
 * Every comparison based strategy sorts through this comparator so a
 * change of criterion is done in a single place. The identifier tie
 * breaker turns the criterion into a total order, which makes every
 * algorithm produce exactly the same result for the same input.
 */
public final class TroopComparator {

    /**
     * Ascending order by attack range, then by identifier.
     */
    public static final Comparator<Troop> BY_RANGE =
            Comparator.comparingInt(Troop::getRange).thenComparing(Troop::getIdentifier);

    private TroopComparator() {
    }
}
