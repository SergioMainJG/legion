package legion.troops.units;

import legion.battlefield.Position;
import legion.troops.Troop;
import legion.troops.TroopType;
import legion.troops.abilities.Attackable;

/**
 * Basic offensive unit. It advances in a straight vertical line and
 * carries a moderate attack power.
 */
public class Infantry extends Troop implements Attackable {

    private static final int MOVEMENT_RANGE = 2;
    private static final int ATTACK_RANGE = 2;
    private static final int ATTACK_POWER = 25;
    private static final String MOVEMENT_PATTERN = "straight";

    /**
     * Creates an infantry unit.
     *
     * @param number sequential number of the unit
     * @param health initial health points
     */
    public Infantry(int number, int health) {
        super(TroopType.INFANTRY, number, health, ATTACK_RANGE);
    }

    /**
     * Advances vertically towards the bottom of the battlefield.
     *
     * @param origin cell where the unit stands
     * @param steps  amount of cells requested
     * @return the destination cell
     */
    @Override
    public Position moveFrom(Position origin, int steps) {
        return origin.shift(0, Math.min(steps, MOVEMENT_RANGE));
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
    public int attack(Troop target) {
        target.receiveDamage(ATTACK_POWER);
        return ATTACK_POWER;
    }

    @Override
    public int getAttackPower() {
        return ATTACK_POWER;
    }
}
