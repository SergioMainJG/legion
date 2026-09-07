package legion.test;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import legion.battlefield.Battlefield;
import legion.battlefield.FormationArranger;
import legion.battlefield.Orientation;
import legion.battlefield.Position;
import legion.battlefield.RandomDeployer;
import legion.sorting.SortingAlgorithm;
import legion.troops.Troop;
import legion.troops.TroopFactory;
import legion.troops.TroopType;

/**
 * Integration checks for the initial deployment and its survival through
 * sorting and formation.
 */
public class DeploymentTests {

    private final RandomDeployer deployer = new RandomDeployer(new TroopFactory());
    private final FormationArranger arranger = new FormationArranger();

    /**
     * Registers the deployment checks in the shared report.
     *
     * @param report accumulator of results
     */
    public void register(TestReport report) {
        report.check("deployment places every requested troop exactly once", () -> {
            Battlefield field = new Battlefield(8);
            List<Troop> troops = deployer.deploy(field, counts(2, 1, 2, 1, 3));
            return troops.size() == 9 && field.deployedTroops().size() == 9;
        });
        report.check("deployment never repeats a position", () -> {
            for (int run = 0; run < 20; run++) {
                Battlefield field = new Battlefield(6);
                deployer.deploy(field, counts(1, 1, 2, 1, 3));
                if (hasDuplicatePosition(field)) {
                    return false;
                }
            }
            return true;
        });
        report.check("deployment stays inside the battlefield on a small field", () -> {
            Battlefield field = new Battlefield(5);
            deployer.deploy(field, counts(5, 5, 5, 5, 5));
            return field.deployedTroops().size() == 25 && everyPositionInside(field);
        });
        report.check("deployment fills a large field with a high troop count", () -> {
            Battlefield field = new Battlefield(40);
            List<Troop> troops = deployer.deploy(field, counts(40, 40, 40, 40, 40, 40, 40, 40));
            return troops.size() == 320 && field.deployedTroops().size() == 320;
        });
        report.check("no troop is lost or renamed between deployment and formation", () -> {
            Battlefield field = new Battlefield(10);
            List<Troop> deployed = deployer.deploy(field, counts(2, 2, 2, 2, 2));
            Set<String> before = identifiers(deployed);
            List<Troop> sorted = SortingAlgorithm.QUICK.createStrategy().sort(deployed);
            arranger.arrange(field, sorted, Orientation.WEST);
            return identifiers(field.deployedTroops()).equals(before);
        });
    }

    private Map<TroopType, Integer> counts(int... values) {
        Map<TroopType, Integer> map = new LinkedHashMap<>();
        TroopType[] types = TroopType.deploymentOrder();
        for (int index = 0; index < types.length; index++) {
            map.put(types[index], index < values.length ? values[index] : 0);
        }
        return map;
    }

    private Set<String> identifiers(List<Troop> troops) {
        Set<String> ids = new HashSet<>();
        for (Troop troop : troops) {
            ids.add(troop.getIdentifier());
        }
        return ids;
    }

    private boolean hasDuplicatePosition(Battlefield field) {
        Set<Position> seen = new HashSet<>();
        for (int row = 0; row < field.getSize(); row++) {
            for (int column = 0; column < field.getSize(); column++) {
                Position position = new Position(column, row);
                if (field.troopAt(position) != null && !seen.add(position)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean everyPositionInside(Battlefield field) {
        for (Troop troop : field.deployedTroops()) {
            if (!field.contains(field.locate(troop.getIdentifier()))) {
                return false;
            }
        }
        return true;
    }
}
