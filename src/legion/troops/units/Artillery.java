package legion.troops.units;

import legion.battlefield.Position;
import legion.troops.Troop;
import legion.troops.TroopType;
import legion.troops.abilities.Attackable;

/**
 * Long range bombardment unit. It has the widest attack range of the
 * roster and stays in place once deployed.
 */
public class Artillery extends Troop implements Attackable {

    private static final int MOVEMENT_RANGE = 0;
    private static final int ATTACK_RANGE = 8;
    private static final int ATTACK_POWER = 60;
    private static final String MOVEMENT_PATTERN = "static";

    /**
     * Creates an artillery unit.
     *
     * @param number sequential number of the unit
     * @param health initial health points
     */
    public Artillery(int number, int health) {
        super(TroopType.ARTILLERY, number, health, ATTACK_RANGE);
    }

    /**
     * Keeps the current cell because the piece is fixed once placed.
     *
     * @param origin cell where the unit stands
     * @param steps  amount of cells requested
     * @return the origin cell unchanged
     */
    @Override
    public Position moveFrom(Position origin, int steps) {
        return origin;
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
