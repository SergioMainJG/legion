package legion.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import legion.sorting.SortDirection;
import legion.sorting.SortingAlgorithm;
import legion.sorting.SortingStrategy;
import legion.sorting.TroopComparator;
import legion.troops.Troop;
import legion.troops.TroopFactory;
import legion.troops.TroopType;

/**
 * Checks for the sorting catalogue.
 */
public class SortingTests {

    private final TroopFactory factory = new TroopFactory();

    /**
     * Registers the sorting checks in the shared report.
     *
     * @param report accumulator of results
     */
    public void register(TestReport report) {
        for (SortingAlgorithm algorithm : SortingAlgorithm.values()) {
            SortingStrategy strategy = algorithm.createStrategy();
            report.check(strategy.getName() + " sorts a mixed list by range", () ->
                    isSorted(strategy.sort(mixedList())));
            report.check(strategy.getName() + " keeps an empty list empty", () ->
                    strategy.sort(new ArrayList<>()).isEmpty());
            report.check(strategy.getName() + " keeps a single element list", () ->
                    strategy.sort(oneTroop()).size() == 1);
            report.check(strategy.getName() + " sorts an already sorted list", () ->
                    isSorted(strategy.sort(strategy.sort(mixedList()))));
            report.check(strategy.getName() + " sorts a reversed list", () -> {
                List<Troop> reversed = mixedList();
                Collections.reverse(reversed);
                return isSorted(strategy.sort(reversed));
            });
            report.check(strategy.getName() + " sorts a list where every range is equal", () ->
                    isSorted(strategy.sort(sameRangeList())));
            report.check(strategy.getName() + " sorts a list mixing the minimum and maximum range", () ->
                    isSorted(strategy.sort(extremeRangeList())));
            report.check(strategy.getName() + " sorts a large dataset", () ->
                    isSorted(strategy.sort(largeList())));
            report.check(strategy.getName() + " orders by range and not by health", () -> {
                List<Troop> sorted = strategy.sort(rangeAgainstHealthList());
                return identifiers(sorted).equals(List.of("T-1", "R-1", "S-1"));
            });
            report.check(strategy.getName() + " does not modify the received list", () -> {
                List<Troop> original = mixedList();
                List<Troop> snapshot = new ArrayList<>(original);
                strategy.sort(original);
                return original.equals(snapshot);
            });
        }
        report.check("every algorithm produces the same ascending order for the same input", () ->
                sameOrder(SortDirection.ASCENDING, mixedList()));
        report.check("every algorithm produces the same descending order for the same input", () ->
                sameOrder(SortDirection.DESCENDING, mixedList()));
        report.check("every algorithm agrees on a dataset with repeated ranges", () ->
                sameOrder(SortDirection.ASCENDING, largeList()));
    }

    private boolean sameOrder(SortDirection direction, List<Troop> source) {
        List<String> reference = identifiers(
                direction.apply(SortingAlgorithm.BUBBLE.createStrategy().sort(source)));
        for (SortingAlgorithm algorithm : SortingAlgorithm.values()) {
            List<String> current = identifiers(
                    direction.apply(algorithm.createStrategy().sort(source)));
            if (!current.equals(reference)) {
                return false;
            }
        }
        return true;
    }

    private List<String> identifiers(List<Troop> troops) {
        List<String> ids = new ArrayList<>();
        for (Troop troop : troops) {
            ids.add(troop.getIdentifier());
        }
        return ids;
    }

    private boolean isSorted(List<Troop> troops) {
        for (int index = 1; index < troops.size(); index++) {
            if (TroopComparator.BY_RANGE.compare(troops.get(index - 1), troops.get(index)) > 0) {
                return false;
            }
        }
        return true;
    }

    private List<Troop> mixedList() {
        List<Troop> troops = new ArrayList<>();
        troops.add(factory.create(TroopType.ARTILLERY, 1));
        troops.add(factory.create(TroopType.MEDIC, 1));
        troops.add(factory.create(TroopType.SNIPER, 1));
        troops.add(factory.create(TroopType.INFANTRY, 1));
        troops.add(factory.create(TroopType.INFANTRY, 2));
        troops.add(factory.create(TroopType.COMMANDER, 1));
        troops.add(factory.create(TroopType.ENGINEER, 1));
        troops.add(factory.create(TroopType.TANK, 1));
        return troops;
    }

    private List<Troop> sameRangeList() {
        List<Troop> troops = new ArrayList<>();
        troops.add(factory.create(TroopType.INFANTRY, 1));
        troops.add(factory.create(TroopType.TANK, 1));
        troops.add(factory.create(TroopType.INFANTRY, 2));
        return troops;
    }

    private List<Troop> extremeRangeList() {
        List<Troop> troops = new ArrayList<>();
        troops.add(factory.create(TroopType.ARTILLERY, 1));
        troops.add(factory.create(TroopType.MEDIC, 1));
        troops.add(factory.create(TroopType.ARTILLERY, 2));
        troops.add(factory.create(TroopType.ENGINEER, 1));
        return troops;
    }

    private List<Troop> largeList() {
        List<Troop> troops = new ArrayList<>();
        TroopType[] types = TroopType.deploymentOrder();
        for (int number = 1; number <= 200; number++) {
            troops.add(factory.create(types[number % types.length], number));
        }
        return troops;
    }

    private List<Troop> rangeAgainstHealthList() {
        List<Troop> troops = new ArrayList<>();
        troops.add(factory.create(TroopType.SNIPER, 1));
        troops.add(factory.create(TroopType.TANK, 1));
        troops.add(factory.create(TroopType.ANTI_AIRCRAFT, 1));
        return troops;
    }

    private List<Troop> oneTroop() {
        List<Troop> troops = new ArrayList<>();
        troops.add(factory.create(TroopType.MEDIC, 1));
        return troops;
    }
}
