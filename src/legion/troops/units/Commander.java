package legion.troops.units;

import legion.battlefield.Position;
import legion.troops.Troop;
import legion.troops.TroopType;
import legion.troops.abilities.Attackable;

/**
 * Leader of the legion. It moves diagonally and is the unit with the
 * highest attack power of the milestone.
 */
public class Commander extends Troop implements Attackable {

    private static final int MOVEMENT_RANGE = 3;
    private static final int ATTACK_POWER = 45;
    private static final String MOVEMENT_PATTERN = "diagonal";

    /**
     * Creates a commander.
     *
     * @param number sequential number of the unit
     * @param health initial health points
     */
    public Commander(int number, int health) {
        super(TroopType.COMMANDER, number, health);
    }

    /**
     * Moves diagonally towards the lower right corner.
     *
     * @param origin cell where the commander stands
     * @param steps  amount of cells requested
     * @return the destination cell
     */
    @Override
    public Position moveFrom(Position origin, int steps) {
        int advance = Math.min(steps, MOVEMENT_RANGE);
        return origin.shift(advance, advance);
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
