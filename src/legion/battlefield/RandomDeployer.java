package legion.battlefield;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import legion.errors.types.BattlefieldSizeException;
import legion.troops.Troop;
import legion.troops.TroopFactory;
import legion.troops.TroopType;

/**
 * Initial deployment of the legion on free random cells.
 * The deployer retries when a cell is already taken and refuses to
 * start when the requested amount of units exceeds the capacity.
 */
public class RandomDeployer {

    private static final int RETRY_FACTOR = 50;

    private final TroopFactory factory;
    private final Random random = new Random();

    /**
     * Creates the deployer with the factory used to build the units.
     *
     * @param factory single creation point of the troops
     */
    public RandomDeployer(TroopFactory factory) {
        this.factory = factory;
    }

    /**
     * Fills the battlefield with the requested amount of units.
     *
     * @param battlefield matrix that receives the units
     * @param counts      amount of units requested per type
     * @return the list of deployed units
     * @throws BattlefieldSizeException when the matrix cannot hold the units
     */
    public List<Troop> deploy(Battlefield battlefield, Map<TroopType, Integer> counts) {
        validateCapacity(battlefield, counts);
        List<Troop> deployed = new ArrayList<>();
        for (Map.Entry<TroopType, Integer> entry : counts.entrySet()) {
            deployGroup(battlefield, entry.getKey(), entry.getValue(), deployed);
        }
        return deployed;
    }

    private void deployGroup(Battlefield battlefield, TroopType type, int amount, List<Troop> deployed) {
        for (int number = 1; number <= amount; number++) {
            Troop troop = factory.create(type, number);
            battlefield.place(findFreePosition(battlefield), troop);
            deployed.add(troop);
        }
    }

    private Position findFreePosition(Battlefield battlefield) {
        int attempts = battlefield.getCapacity() * RETRY_FACTOR;
        for (int attempt = 0; attempt < attempts; attempt++) {
            Position candidate = randomPosition(battlefield.getSize());
            if (!battlefield.isOccupied(candidate)) {
                return candidate;
            }
        }
        throw new BattlefieldSizeException("No free cell was found after " + attempts + " attempts.");
    }

    private Position randomPosition(int size) {
        return new Position(random.nextInt(size), random.nextInt(size));
    }

    private void validateCapacity(Battlefield battlefield, Map<TroopType, Integer> counts) {
        int total = counts.values().stream().mapToInt(Integer::intValue).sum();
        if (total > battlefield.getCapacity()) {
            throw new BattlefieldSizeException("The battlefield holds " + battlefield.getCapacity()
                    + " cells and " + total + " troops were requested.");
        }
    }
}
