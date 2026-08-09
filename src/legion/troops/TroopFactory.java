package legion.troops;

import java.util.Random;
import legion.troops.units.Commander;
import legion.troops.units.Infantry;
import legion.troops.units.Medic;

/**
 * Single creation point of the units.
 * The rest of the application depends on the abstraction and never
 * instantiates a concrete unit directly.
 */
public class TroopFactory {

    private static final int COMMANDER_BASE_HEALTH = 180;
    private static final int MEDIC_BASE_HEALTH = 110;
    private static final int INFANTRY_BASE_HEALTH = 140;
    private static final int HEALTH_VARIATION = 40;
    private static final String NOT_IMPLEMENTED = " is not implemented yet";

    private final Random random = new Random();

    /**
     * Creates a unit of the requested type.
     *
     * @param type   catalogue entry of the unit
     * @param number sequential number of the unit
     * @return a new troop instance
     * @throws IllegalArgumentException when the type has no concrete unit
     */
    public Troop create(TroopType type, int number) {
        return switch (type) {
            case COMMANDER -> new Commander(number, varyHealth(COMMANDER_BASE_HEALTH));
            case MEDIC -> new Medic(number, varyHealth(MEDIC_BASE_HEALTH));
            case INFANTRY -> new Infantry(number, varyHealth(INFANTRY_BASE_HEALTH));
            case TANK -> throw new IllegalArgumentException(TroopType.TANK.getLabel() + NOT_IMPLEMENTED);
            case SNIPER -> throw new IllegalArgumentException(TroopType.SNIPER.getLabel() + NOT_IMPLEMENTED);
        };
    }

    private int varyHealth(int baseHealth) {
        return baseHealth + random.nextInt(HEALTH_VARIATION);
    }
}
