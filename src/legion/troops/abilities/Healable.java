package legion.troops.abilities;

import legion.troops.Troop;

/**
 * Behaviour injected only into support troops able to restore health
 * to an ally. Offensive units never implement this contract.
 */
public interface Healable {

    /**
     * Restores health to the target troop.
     *
     * @param target troop that receives the healing
     * @return the amount of health restored
     */
    int heal(Troop target);

    /**
     * Returns the raw healing amount of the troop.
     *
     * @return the healing power
     */
    int getHealingPower();
}
