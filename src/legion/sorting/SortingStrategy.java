package legion.sorting;

import java.util.List;
import legion.troops.Troop;

/**
 * Contract of every sorting algorithm of the simulator.
 * Adding a new algorithm only requires a new implementation of this
 * interface, existing code is never modified.
 */
public interface SortingStrategy {

    /**
     * Sorts the units by ascending health.
     *
     * @param troops units to sort
     * @return a new list with the units in ascending order
     */
    List<Troop> sort(List<Troop> troops);

    /**
     * Returns the readable name of the algorithm.
     *
     * @return the name shown in the console
     */
    String getName();
}
