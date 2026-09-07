package legion.troops.units;

import legion.battlefield.Position;
import legion.troops.Troop;
import legion.troops.TroopType;
import legion.troops.abilities.Healable;

/**
 * Field support unit. It repairs allied units, which is modelled as a
 * healing behaviour with a lower yield than the medic.
 */
public class Engineer extends Troop implements Healable {

    private static final int MOVEMENT_RANGE = 2;
    private static final int ATTACK_RANGE = 1;
    private static final int REPAIR_POWER = 20;
    private static final String MOVEMENT_PATTERN = "lateral";

    /**
     * Creates an engineer unit.
     *
     * @param number sequential number of the unit
     * @param health initial health points
     */
    public Engineer(int number, int health) {
        super(TroopType.ENGINEER, number, health, ATTACK_RANGE);
    }

    /**
     * Advances horizontally to reach the unit it must repair.
     *
     * @param origin cell where the unit stands
     * @param steps  amount of cells requested
     * @return the destination cell
     */
    @Override
    public Position moveFrom(Position origin, int steps) {
        return origin.shift(Math.min(steps, MOVEMENT_RANGE), 0);
    }

    @Override
    public int getMovementRange() {
        return MOVEMENT_RANGE;
    }

    @Override
    public String describeMovement() {
        return MOVEMENT_PATTERN;
    }

    @Override
    public int heal(Troop target) {
        target.receiveHealing(REPAIR_POWER);
        return REPAIR_POWER;
    }

    @Override
    public int getHealingPower() {
        return REPAIR_POWER;
    }
}
