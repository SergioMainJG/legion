package legion.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import legion.battlefield.Battlefield;
import legion.battlefield.FormationArranger;
import legion.battlefield.Orientation;
import legion.battlefield.RandomDeployer;
import legion.setup.LaunchParameters;
import legion.setup.ParameterParser;
import legion.setup.ParameterValidator;
import legion.sorting.SortDirection;
import legion.sorting.SortingAlgorithm;
import legion.troops.Troop;
import legion.troops.TroopFactory;
import legion.troops.TroopType;

/**
 * Regression checks that lock the behaviour approved in the midterm so
 * the final corrections cannot silently break it.
 */
public class RegressionTests {

    private final ParameterParser parser = new ParameterParser();
    private final ParameterValidator validator = new ParameterValidator();
    private final TroopFactory factory = new TroopFactory();

    /**
     * Registers the regression checks in the shared report.
     *
     * @param report accumulator of results
     */
    public void register(TestReport report) {
        report.check("a midterm style command still runs the full pipeline", () -> {
            LaunchParameters parameters = parser.parse(new String[] {"a=b", "t=c", "o=s", "u=1,1,2", "f=6"});
            validator.validate(parameters);
            Battlefield field = new Battlefield(parameters.getFieldSize());
            RandomDeployer deployer = new RandomDeployer(new TroopFactory());
            List<Troop> troops = deployer.deploy(field, parameters.getTroopCounts());
            List<Troop> ascending = parameters.getAlgorithm().createStrategy().sort(troops);
            List<Troop> ordered = parameters.getDirection().apply(ascending);
            new FormationArranger().arrange(field, ordered, parameters.getOrientation());
            return field.deployedTroops().size() == 4;
        });
        report.check("bubble and insertion still agree with the other algorithms", () -> {
            List<Troop> data = List.of(
                    factory.create(TroopType.SNIPER, 1),
                    factory.create(TroopType.MEDIC, 1),
                    factory.create(TroopType.INFANTRY, 1),
                    factory.create(TroopType.COMMANDER, 1));
            List<Troop> bubble = SortingAlgorithm.BUBBLE.createStrategy().sort(data);
            List<Troop> insertion = SortingAlgorithm.INSERTION.createStrategy().sort(data);
            List<Troop> merge = SortingAlgorithm.MERGE.createStrategy().sort(data);
            return bubble.equals(insertion) && bubble.equals(merge);
        });
        report.check("the four orientation keys still resolve", () ->
                Orientation.fromKey("n") == Orientation.NORTH
                        && Orientation.fromKey("s") == Orientation.SOUTH
                        && Orientation.fromKey("e") == Orientation.EAST
                        && Orientation.fromKey("w") == Orientation.WEST);
        report.check("both sort directions still resolve", () ->
                SortDirection.fromKey("c") == SortDirection.ASCENDING
                        && SortDirection.fromKey("d") == SortDirection.DESCENDING);
        report.check("descending is the exact reverse of ascending", () -> {
            List<Troop> data = List.of(
                    factory.create(TroopType.ARTILLERY, 1),
                    factory.create(TroopType.MEDIC, 1),
                    factory.create(TroopType.TANK, 1));
            List<Troop> ascending = SortingAlgorithm.HEAP.createStrategy().sort(data);
            List<Troop> descending = SortDirection.DESCENDING.apply(ascending);
            List<Troop> reversed = new ArrayList<>(ascending);
            Collections.reverse(reversed);
            return descending.equals(reversed);
        });
    }
}
