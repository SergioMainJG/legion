package legion.sorting;

import java.util.function.Supplier;
import legion.errors.types.InvalidAlgorithmException;
import legion.sorting.strategies.BubbleSortStrategy;
import legion.sorting.strategies.CountingSortStrategy;
import legion.sorting.strategies.HeapSortStrategy;
import legion.sorting.strategies.InsertionSortStrategy;
import legion.sorting.strategies.MergeSortStrategy;
import legion.sorting.strategies.QuickSortStrategy;
import legion.sorting.strategies.RadixSortStrategy;
import legion.sorting.strategies.SelectionSortStrategy;

/**
 * Catalogue that binds the command line key of the parameter a with
 * the strategy that must be instantiated. Every entry is backed by a
 * working implementation.
 */
public enum SortingAlgorithm {

    BUBBLE("b", BubbleSortStrategy::new),
    INSERTION("i", InsertionSortStrategy::new),
    SELECTION("s", SelectionSortStrategy::new),
    MERGE("m", MergeSortStrategy::new),
    QUICK("q", QuickSortStrategy::new),
    HEAP("h", HeapSortStrategy::new),
    COUNTING("c", CountingSortStrategy::new),
    RADIX("r", RadixSortStrategy::new);

    private final String key;
    private final Supplier<SortingStrategy> creator;

    SortingAlgorithm(String key, Supplier<SortingStrategy> creator) {
        this.key = key;
        this.creator = creator;
    }

    /**
     * Returns the command line key of the algorithm.
     *
     * @return the key expected in the parameter a
     */
    public String getKey() {
        return key;
    }

    /**
     * Builds the strategy associated with this entry.
     *
     * @return a new strategy instance
     */
    public SortingStrategy createStrategy() {
        return creator.get();
    }

    /**
     * Resolves the catalogue entry that matches a command line key.
     *
     * @param key value received in the parameter a
     * @return the matching entry
     * @throws InvalidAlgorithmException when no entry matches the key
     */
    public static SortingAlgorithm fromKey(String key) {
        for (SortingAlgorithm algorithm : values()) {
            if (algorithm.key.equalsIgnoreCase(key)) {
                return algorithm;
            }
        }
        throw new InvalidAlgorithmException(key);
    }
}
