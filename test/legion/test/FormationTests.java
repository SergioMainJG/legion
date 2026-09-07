package legion.test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import legion.battlefield.Battlefield;
import legion.battlefield.FormationArranger;
import legion.battlefield.Orientation;
import legion.battlefield.Position;
import legion.sorting.SortingAlgorithm;
import legion.troops.Troop;
import legion.troops.TroopFactory;
import legion.troops.TroopType;

/**
 * Checks for the final formation placement.
 */
public class FormationTests {

    private final TroopFactory factory = new TroopFactory();
    private final FormationArranger arranger = new FormationArranger();

    /**
     * Registers the formation checks in the shared report.
     *
     * @param report accumulator of results
     */
    public void register(TestReport report) {
        report.check("north builds the first group against the south edge", () -> {
            Battlefield field = arrange(Orientation.NORTH);
            return typeAtRow(field, field.getSize() - 1) == TroopType.MEDIC;
        });
        report.check("south builds the first group against the north edge", () -> {
            Battlefield field = arrange(Orientation.SOUTH);
            return typeAtRow(field, 0) == TroopType.MEDIC;
        });
        report.check("east builds the first group against the west edge", () -> {
            Battlefield field = arrange(Orientation.EAST);
            return typeAtColumn(field, 0) == TroopType.MEDIC;
        });
        report.check("west builds the first group against the east edge", () -> {
            Battlefield field = arrange(Orientation.WEST);
            return typeAtColumn(field, field.getSize() - 1) == TroopType.MEDIC;
        });
        report.check("no line mixes two troop types", () -> {
            for (Orientation orientation : Orientation.values()) {
                if (mixesTypes(arrange(orientation), orientation)) {
                    return false;
                }
            }
            return true;
        });
    }

    private Battlefield arrange(Orientation orientation) {
        Battlefield field = new Battlefield(8);
        List<Troop> sorted = SortingAlgorithm.MERGE.createStrategy().sort(sample());
        arranger.arrange(field, sorted, orientation);
        return field;
    }

    private List<Troop> sample() {
        List<Troop> troops = new ArrayList<>();
        troops.add(factory.create(TroopType.MEDIC, 1));
        troops.add(factory.create(TroopType.INFANTRY, 1));
        troops.add(factory.create(TroopType.INFANTRY, 2));
        troops.add(factory.create(TroopType.COMMANDER, 1));
        troops.add(factory.create(TroopType.SNIPER, 1));
        return troops;
    }

    private TroopType typeAtRow(Battlefield field, int row) {
        for (int column = 0; column < field.getSize(); column++) {
            Troop troop = field.troopAt(new Position(column, row));
            if (troop != null) {
                return troop.getType();
            }
        }
        return null;
    }

    private TroopType typeAtColumn(Battlefield field, int column) {
        for (int row = 0; row < field.getSize(); row++) {
            Troop troop = field.troopAt(new Position(column, row));
            if (troop != null) {
                return troop.getType();
            }
        }
        return null;
    }

    private boolean mixesTypes(Battlefield field, Orientation orientation) {
        for (int line = 0; line < field.getSize(); line++) {
            Set<TroopType> types = new HashSet<>();
            for (int index = 0; index < field.getSize(); index++) {
                Position position = orientation.isVertical()
                        ? new Position(line, index)
                        : new Position(index, line);
                Troop troop = field.troopAt(position);
                if (troop != null) {
                    types.add(troop.getType());
                }
            }
            if (types.size() > 1) {
                return true;
            }
        }
        return false;
    }
}
