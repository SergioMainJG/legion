package legion.troops.units;

import legion.battlefield.Position;
import legion.troops.Troop;
import legion.troops.TroopType;
import legion.troops.abilities.Healable;

/**
 * Support unit. It moves sideways and restores health to allies, and
 * it is the only unit of the milestone that implements the healing
 * behaviour.
 */
public class Medic extends Troop implements Healable {

    private static final int MOVEMENT_RANGE = 1;
    private static final int HEALING_POWER = 30;
    private static final String MOVEMENT_PATTERN = "lateral";

    /**
     * Creates a medic unit.
     *
     * @param number sequential number of the unit
     * @param health initial health points
     */
    public Medic(int number, int health) {
        super(TroopType.MEDIC, number, health);
    }

    /**
     * Advances horizontally towards the right side of the battlefield.
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
        target.receiveHealing(HEALING_POWER);
        return HEALING_POWER;
    }

    @Override
    public int getHealingPower() {
        return HEALING_POWER;
    }
}
