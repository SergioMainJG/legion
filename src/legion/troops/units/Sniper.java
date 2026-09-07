package legion.troops.units;

import legion.battlefield.Position;
import legion.troops.Troop;
import legion.troops.TroopType;
import legion.troops.abilities.Attackable;

/**
 * Precision marksman. It has a long attack range and a light frame,
 * trading survivability for reach.
 */
public class Sniper extends Troop implements Attackable {

    private static final int MOVEMENT_RANGE = 2;
    private static final int ATTACK_RANGE = 6;
    private static final int ATTACK_POWER = 35;
    private static final String MOVEMENT_PATTERN = "straight";

    /**
     * Creates a sniper unit.
     *
     * @param number sequential number of the unit
     * @param health initial health points
     */
    public Sniper(int number, int health) {
        super(TroopType.SNIPER, number, health, ATTACK_RANGE);
    }

    /**
     * Advances vertically while keeping distance from the front line.
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
