package legion.test;

import legion.battlefield.Battlefield;
import legion.battlefield.Position;
import legion.errors.types.BattlefieldSizeException;
import legion.troops.Troop;
import legion.troops.TroopFactory;
import legion.troops.TroopType;

/**
 * Checks for the battlefield matrix.
 */
public class BattlefieldTests {

    private final TroopFactory factory = new TroopFactory();

    /**
     * Registers the battlefield checks in the shared report.
     *
     * @param report accumulator of results
     */
    public void register(TestReport report) {
        report.check("battlefield accepts a position inside the bounds", () -> {
            Battlefield battlefield = new Battlefield(6);
            battlefield.place(new Position(2, 3), troop());
            return battlefield.isOccupied(new Position(2, 3));
        });
        report.expectFailure("battlefield rejects a position outside the bounds",
                BattlefieldSizeException.class, () ->
                        new Battlefield(6).place(new Position(6, 0), troop()));
        report.expectFailure("battlefield rejects a second troop on a taken cell",
                BattlefieldSizeException.class, () -> {
                    Battlefield battlefield = new Battlefield(6);
                    battlefield.place(new Position(1, 1), troop());
                    battlefield.place(new Position(1, 1), troop());
                });
        report.check("battlefield reports its total capacity", () ->
                new Battlefield(7).getCapacity() == 49);
        report.check("battlefield accepts the minimum size", () ->
                new Battlefield(Battlefield.MINIMUM_SIZE).getSize() == 5);
        report.check("battlefield accepts the maximum size", () ->
                new Battlefield(Battlefield.MAXIMUM_SIZE).getSize() == 1000);
        report.expectFailure("battlefield rejects a size below the minimum",
                BattlefieldSizeException.class, () -> new Battlefield(4));
        report.expectFailure("battlefield rejects a size above the maximum",
                BattlefieldSizeException.class, () -> new Battlefield(1001));
    }

    private Troop troop() {
        return factory.create(TroopType.INFANTRY, 1);
    }
}
