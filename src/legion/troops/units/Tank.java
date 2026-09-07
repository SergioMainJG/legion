package legion.troops.units;

import legion.battlefield.Position;
import legion.troops.Troop;
import legion.troops.TroopType;
import legion.troops.abilities.Attackable;

/**
 * Heavy armoured unit. It advances slowly in a straight line and
 * absorbs far more damage than any other unit of the roster.
 */
public class Tank extends Troop implements Attackable {

    private static final int MOVEMENT_RANGE = 1;
    private static final int ATTACK_RANGE = 2;
    private static final int ATTACK_POWER = 40;
    private static final String MOVEMENT_PATTERN = "straight";

    /**
     * Creates a tank unit.
     *
     * @param number sequential number of the unit
     * @param health initial health points
     */
    public Tank(int number, int health) {
        super(TroopType.TANK, number, health, ATTACK_RANGE);
    }

    /**
     * Advances a single cell downwards because of its weight.
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
