package legion.test;

import legion.errors.types.InvalidAlgorithmException;
import legion.errors.types.InvalidParameterException;
import legion.setup.LaunchParameters;
import legion.setup.ParameterParser;
import legion.troops.TroopType;

/**
 * Checks for the command line parser.
 */
public class ParserTests {

    private final ParameterParser parser = new ParameterParser();

    /**
     * Registers the parser checks in the shared report.
     *
     * @param report accumulator of results
     */
    public void register(TestReport report) {
        report.check("parser accepts a valid argument line", () -> {
            LaunchParameters parameters = parser.parse(new String[] {"a=b", "t=c", "o=s", "u=1,1,2", "f=10"});
            return parameters.getFieldSize() == 10 && parameters.getTotalTroops() == 4;
        });
        report.check("parser defaults the field size to 10 when f is absent", () ->
                parser.parse(new String[] {"a=b", "t=c", "o=s", "u=1"}).getFieldSize() == 10);
        report.check("parser maps a short u list and pads the rest with zero", () -> {
            LaunchParameters parameters = parser.parse(new String[] {"a=b", "t=c", "o=s", "u=3"});
            return parameters.getTroopCounts().get(TroopType.COMMANDER) == 3
                    && parameters.getTroopCounts().get(TroopType.ANTI_AIRCRAFT) == 0;
        });
        report.expectFailure("parser rejects a missing mandatory parameter",
                InvalidParameterException.class,
                () -> parser.parse(new String[] {"a=b", "t=c", "o=s"}));
        report.expectFailure("parser rejects an argument without key=value shape",
                InvalidParameterException.class,
                () -> parser.parse(new String[] {"a=b", "t=c", "o=s", "u=1", "bogus"}));
        report.expectFailure("parser rejects a duplicated key",
                InvalidParameterException.class,
                () -> parser.parse(new String[] {"a=b", "a=i", "t=c", "o=s", "u=1"}));
        report.expectFailure("parser rejects an unknown algorithm key",
                InvalidAlgorithmException.class,
                () -> parser.parse(new String[] {"a=z", "t=c", "o=s", "u=1"}));
        report.expectFailure("parser rejects a non numeric count",
                InvalidParameterException.class,
                () -> parser.parse(new String[] {"a=b", "t=c", "o=s", "u=1,x"}));
        report.expectFailure("parser rejects a negative count",
                InvalidParameterException.class,
                () -> parser.parse(new String[] {"a=b", "t=c", "o=s", "u=1,-2"}));
        report.expectFailure("parser rejects an unknown parameter",
                InvalidParameterException.class,
                () -> parser.parse(new String[] {"a=b", "t=c", "o=s", "u=1", "x=5"}));
        report.expectFailure("parser rejects an unknown sort direction value",
                InvalidParameterException.class,
                () -> parser.parse(new String[] {"a=b", "t=x", "o=s", "u=1"}));
        report.expectFailure("parser rejects an unknown orientation value",
                InvalidParameterException.class,
                () -> parser.parse(new String[] {"a=b", "t=c", "o=x", "u=1"}));
        report.expectFailure("parser rejects a non numeric field size",
                InvalidParameterException.class,
                () -> parser.parse(new String[] {"a=b", "t=c", "o=s", "u=1", "f=big"}));
        report.check("parser is case insensitive for keys and enum values", () -> {
            LaunchParameters parameters = parser.parse(new String[] {"A=B", "T=C", "O=S", "U=1,1"});
            return parameters.getTotalTroops() == 2;
        });
    }
}
