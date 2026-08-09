package legion.troops.abilities;

import legion.troops.Troop;

/**
 * Behaviour injected only into the troops that are allowed to deal
 * damage. Support units never implement this contract.
 */
public interface Attackable {

    /**
     * Applies damage to the target troop.
     *
     * @param target troop that receives the attack
     * @return the amount of damage applied
     */
    int attack(Troop target);

    /**
     * Returns the raw damage of the troop.
     *
     * @return the attack power
     */
    int getAttackPower();
}
