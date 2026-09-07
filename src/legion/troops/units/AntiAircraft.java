package legion.troops.units;

import legion.battlefield.Position;
import legion.troops.Troop;
import legion.troops.TroopType;
import legion.troops.abilities.Attackable;

/**
 * Air defence unit. It tracks targets sideways and keeps a wide attack
 * range to cover the formation.
 */
public class AntiAircraft extends Troop implements Attackable {

    private static final int MOVEMENT_RANGE = 2;
    private static final int ATTACK_RANGE = 5;
    private static final int ATTACK_POWER = 38;
    private static final String MOVEMENT_PATTERN = "lateral";

    /**
     * Creates an anti aircraft unit.
     *
     * @param number sequential number of the unit
     * @param health initial health points
     */
    public AntiAircraft(int number, int health) {
        super(TroopType.ANTI_AIRCRAFT, number, health, ATTACK_RANGE);
    }

    /**
     * Traverses horizontally to align with the incoming target.
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
    public int attack(Troop target) {
        target.receiveDamage(ATTACK_POWER);
        return ATTACK_POWER;
    }

    @Override
    public int getAttackPower() {
        return ATTACK_POWER;
    }
}
