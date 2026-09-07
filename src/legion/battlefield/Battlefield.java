package legion.battlefield;

import java.util.ArrayList;
import java.util.List;
import legion.errors.types.BattlefieldSizeException;
import legion.troops.Troop;

/**
 * Square matrix that holds the deployed units.
 * The matrix is the only owner of the cells, so every query and every
 * change of position passes through this class.
 */
public class Battlefield {

    /**
     * Smallest side length accepted for the matrix.
     */
    public static final int MINIMUM_SIZE = 5;

    /**
     * Largest side length accepted for the matrix.
     */
    public static final int MAXIMUM_SIZE = 1000;

    /**
     * Side length used when the parameter f is omitted.
     */
    public static final int DEFAULT_SIZE = 10;

    private final int size;
    private final Troop[][] cells;

    /**
     * Creates an empty battlefield.
     *
     * @param size amount of rows and columns of the matrix
     * @throws BattlefieldSizeException when the size is out of range
     */
    public Battlefield(int size) {
        validateSize(size);
        this.size = size;
        this.cells = new Troop[size][size];
    }

    /**
     * Returns the amount of rows and columns of the matrix.
     *
     * @return the size of the battlefield
     */
    public int getSize() {
        return size;
    }

    /**
     * Returns the total amount of cells available.
     *
     * @return the capacity of the battlefield
     */
    public int getCapacity() {
        return size * size;
    }

    /**
     * Indicates whether a coordinate belongs to the matrix.
     *
     * @param position coordinate to test
     * @return true when the coordinate is inside the bounds
     */
    public boolean contains(Position position) {
        return position.x() >= 0 && position.x() < size
                && position.y() >= 0 && position.y() < size;
    }

    /**
     * Indicates whether a cell already holds a unit.
     *
     * @param position coordinate to test
     * @return true when the cell is taken
     */
    public boolean isOccupied(Position position) {
        return contains(position) && cells[position.y()][position.x()] != null;
    }

    /**
     * Places a unit in a free cell.
     *
     * @param position destination coordinate
     * @param troop    unit to place
     * @throws BattlefieldSizeException when the cell is outside the matrix
     *                                  or is already occupied
     */
    public void place(Position position, Troop troop) {
        if (!contains(position)) {
            throw new BattlefieldSizeException("Position " + position + " is outside the battlefield.");
        }
        if (cells[position.y()][position.x()] != null) {
            throw new BattlefieldSizeException("Position " + position + " is already occupied.");
        }
        cells[position.y()][position.x()] = troop;
    }

    /**
     * Returns the unit stored in a cell.
     *
     * @param position coordinate to read
     * @return the unit of the cell or null when it is empty
     */
    public Troop troopAt(Position position) {
        if (!contains(position)) {
            return null;
        }
        return cells[position.y()][position.x()];
    }

    /**
     * Removes the unit stored in a cell.
     *
     * @param position coordinate to release
     */
    public void release(Position position) {
        if (contains(position)) {
            cells[position.y()][position.x()] = null;
        }
    }

    /**
     * Returns the coordinate of a unit identified by its code.
     *
     * @param identifier code of the unit
     * @return the coordinate of the unit or null when it is not deployed
     */
    public Position locate(String identifier) {
        for (int row = 0; row < size; row++) {
            for (int column = 0; column < size; column++) {
                Troop troop = cells[row][column];
                if (troop != null && troop.getIdentifier().equalsIgnoreCase(identifier)) {
                    return new Position(column, row);
                }
            }
        }
        return null;
    }

    /**
     * Returns every deployed unit in reading order.
     *
     * @return the list of units currently on the matrix
     */
    public List<Troop> deployedTroops() {
        List<Troop> troops = new ArrayList<>();
        for (int row = 0; row < size; row++) {
            for (int column = 0; column < size; column++) {
                if (cells[row][column] != null) {
                    troops.add(cells[row][column]);
                }
            }
        }
        return troops;
    }

    /**
     * Empties every cell of the matrix.
     */
    public void clear() {
        for (int row = 0; row < size; row++) {
            for (int column = 0; column < size; column++) {
                cells[row][column] = null;
            }
        }
    }

    private void validateSize(int size) {
        if (size < MINIMUM_SIZE || size > MAXIMUM_SIZE) {
            throw new BattlefieldSizeException("Field size must be between "
                    + MINIMUM_SIZE + " and " + MAXIMUM_SIZE + ", received " + size + ".");
        }
    }
}
