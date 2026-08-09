package legion.troops.abilities;

import legion.battlefield.Position;

/**
 * Behaviour injected into every troop that is able to change its
 * position on the battlefield. Each unit resolves its own movement
 * pattern, which is the polymorphic part of the contract.
 */
public interface Movable {

    /**
     * Calculates the destination reached from an origin position.
     *
     * @param origin cell where the troop stands
     * @param steps  amount of cells the troop tries to advance
     * @return the destination cell according to the movement pattern
     */
    Position moveFrom(Position origin, int steps);

    /**
     * Returns the maximum amount of cells the troop can advance.
     *
     * @return the movement range in cells
     */
    int getMovementRange();

    /**
     * Describes the movement pattern of the troop.
     *
     * @return a short description of the pattern
     */
    String describeMovement();
}
