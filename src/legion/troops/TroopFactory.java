package legion.troops;

import java.util.Random;
import legion.troops.units.AntiAircraft;
import legion.troops.units.Artillery;
import legion.troops.units.Commander;
import legion.troops.units.Engineer;
import legion.troops.units.Infantry;
import legion.troops.units.Medic;
import legion.troops.units.Sniper;
import legion.troops.units.Tank;

/**
 * Single creation point of the units.
 * The rest of the application depends on the abstraction and never
 * instantiates a concrete unit directly.
 */
public class TroopFactory {

    private static final int COMMANDER_BASE_HEALTH = 180;
    private static final int MEDIC_BASE_HEALTH = 110;
    private static final int TANK_BASE_HEALTH = 220;
    private static final int SNIPER_BASE_HEALTH = 120;
    private static final int INFANTRY_BASE_HEALTH = 140;
    private static final int ENGINEER_BASE_HEALTH = 130;
    private static final int ARTILLERY_BASE_HEALTH = 100;
    private static final int ANTI_AIRCRAFT_BASE_HEALTH = 150;
    private static final int HEALTH_VARIATION = 40;

    private final Random random = new Random();

    /**
     * Creates a unit of the requested type.
     *
     * @param type   catalogue entry of the unit
     * @param number sequential number of the unit
     * @return a new troop instance
     */
    public Troop create(TroopType type, int number) {
        return switch (type) {
            case COMMANDER -> new Commander(number, varyHealth(COMMANDER_BASE_HEALTH));
            case MEDIC -> new Medic(number, varyHealth(MEDIC_BASE_HEALTH));
            case TANK -> new Tank(number, varyHealth(TANK_BASE_HEALTH));
            case SNIPER -> new Sniper(number, varyHealth(SNIPER_BASE_HEALTH));
            case INFANTRY -> new Infantry(number, varyHealth(INFANTRY_BASE_HEALTH));
            case ENGINEER -> new Engineer(number, varyHealth(ENGINEER_BASE_HEALTH));
            case ARTILLERY -> new Artillery(number, varyHealth(ARTILLERY_BASE_HEALTH));
            case ANTI_AIRCRAFT -> new AntiAircraft(number, varyHealth(ANTI_AIRCRAFT_BASE_HEALTH));
        };
    }

    private int varyHealth(int baseHealth) {
        return baseHealth + random.nextInt(HEALTH_VARIATION);
    }
}
