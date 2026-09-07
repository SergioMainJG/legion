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
     * Sorts the units by ascending attack range.
     * The received list is never modified, the result is a new list.
     *
     * @param troops units to sort
     * @return a new list with the units in ascending range order
     */
    List<Troop> sort(List<Troop> troops);

    /**
     * Returns the readable name of the algorithm.
     *
     * @return the name shown in the console
     */
    String getName();
}
