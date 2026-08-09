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

/**
 * Catalogue that binds the command line key of the parameter a with
 * the strategy that must be instantiated.
 */
public enum SortingAlgorithm {

    BUBBLE("b", BubbleSortStrategy::new, true),
    INSERTION("i", InsertionSortStrategy::new, true),
    QUICK("q", QuickSortStrategy::new, false),
    MERGE("m", MergeSortStrategy::new, false),
    HEAP("h", HeapSortStrategy::new, false),
    COUNTING("c", CountingSortStrategy::new, false),
    RADIX("r", RadixSortStrategy::new, false);

    private static final String KEY_SEPARATOR = ", ";

    private final String key;
    private final Supplier<SortingStrategy> creator;
    private final boolean implemented;

    SortingAlgorithm(String key, Supplier<SortingStrategy> creator, boolean implemented) {
        this.key = key;
        this.creator = creator;
        this.implemented = implemented;
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
     * Indicates whether the algorithm has a working implementation.
     *
     * @return true when the strategy can sort
     */
    public boolean isImplemented() {
        return implemented;
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

    /**
     * Returns the keys of the algorithms that already work.
     *
     * @return the available keys separated by commas
     */
    public static String implementedKeys() {
        StringBuilder keys = new StringBuilder();
        for (SortingAlgorithm algorithm : values()) {
            if (algorithm.implemented) {
                keys.append(algorithm.key).append(KEY_SEPARATOR);
            }
        }
        return keys.substring(0, keys.length() - KEY_SEPARATOR.length());
    }
}
