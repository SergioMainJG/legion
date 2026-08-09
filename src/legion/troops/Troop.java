package legion.troops;

import legion.troops.abilities.Movable;

/**
 * Common abstraction of every unit of the legion.
 * State is fully private and only exposed through accessors, and the
 * constructor is protected so instances are created by the concrete
 * units through the factory.
 */
public abstract class Troop implements Movable {

    private static final int MINIMUM_HEALTH = 0;
    private static final String IDENTIFIER_SEPARATOR = "-";

    private final String identifier;
    private final TroopType type;
    private final int maximumHealth;
    private int health;

    /**
     * Initialises the shared state of a unit.
     *
     * @param type   catalogue entry of the unit
     * @param number sequential number used to build the identifier
     * @param health initial and maximum health of the unit
     */
    protected Troop(TroopType type, int number, int health) {
        this.type = type;
        this.identifier = type.getSymbol() + IDENTIFIER_SEPARATOR + number;
        this.maximumHealth = health;
        this.health = health;
    }

    /**
     * Returns the unique identifier of the unit.
     *
     * @return the identifier used by the interactive commands
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * Returns the catalogue entry of the unit.
     *
     * @return the type of the troop
     */
    public TroopType getType() {
        return type;
    }

    /**
     * Returns the current health of the unit.
     *
     * @return the remaining health points
     */
    public int getHealth() {
        return health;
    }

    /**
     * Returns the health the unit had when it was created.
     *
     * @return the maximum health points
     */
    public int getMaximumHealth() {
        return maximumHealth;
    }

    /**
     * Subtracts damage from the current health without going below zero.
     *
     * @param damage amount of damage received
     */
    public void receiveDamage(int damage) {
        health = Math.max(MINIMUM_HEALTH, health - damage);
    }

    /**
     * Adds health to the unit without exceeding its maximum.
     *
     * @param amount amount of health restored
     */
    public void receiveHealing(int amount) {
        health = Math.min(maximumHealth, health + amount);
    }

    /**
     * Indicates whether the unit still has health points.
     *
     * @return true when the unit is able to act
     */
    public boolean isAlive() {
        return health > MINIMUM_HEALTH;
    }

    /**
     * Builds the status line of the unit shown by the console.
     *
     * @return a single line describing the current state
     */
    public String getStatus() {
        return identifier + " " + type.getLabel()
                + " health=" + health + "/" + maximumHealth
                + " range=" + getMovementRange()
                + " pattern=" + describeMovement();
    }

    /**
     * Returns the short representation used inside listings.
     *
     * @return the identifier and the health of the unit
     */
    @Override
    public String toString() {
        return identifier + "(" + health + ")";
    }
}
