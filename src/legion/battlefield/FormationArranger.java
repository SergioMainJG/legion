package legion.battlefield;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import legion.troops.Troop;
import legion.troops.TroopType;

/**
 * Placement of the sorted legion into its final formation.
 * Every type takes its own line and the orientation decides the edge
 * where the formation starts and whether the lines are rows or columns.
 */
public class FormationArranger {

    /**
     * Rebuilds the battlefield with the sorted units in formation.
     *
     * @param battlefield matrix to rebuild
     * @param sorted      units already ordered by the strategy
     * @param orientation edge where the formation is built
     */
    public void arrange(Battlefield battlefield, List<Troop> sorted, Orientation orientation) {
        battlefield.clear();
        Map<TroopType, List<Troop>> groups = groupByType(sorted);
        int line = 0;
        for (List<Troop> group : groups.values()) {
            placeLine(battlefield, group, resolveLine(battlefield, line, orientation), orientation);
            line++;
        }
    }

    private Map<TroopType, List<Troop>> groupByType(List<Troop> sorted) {
        Map<TroopType, List<Troop>> groups = new LinkedHashMap<>();
        for (Troop troop : sorted) {
            groups.computeIfAbsent(troop.getType(), type -> new ArrayList<>()).add(troop);
        }
        return groups;
    }

    private int resolveLine(Battlefield battlefield, int line, Orientation orientation) {
        if (orientation.isReversed()) {
            return battlefield.getSize() - 1 - line;
        }
        return line;
    }

    private void placeLine(Battlefield battlefield, List<Troop> group, int line, Orientation orientation) {
        for (int index = 0; index < group.size(); index++) {
            battlefield.place(resolvePosition(line, index, orientation), group.get(index));
        }
    }

    private Position resolvePosition(int line, int index, Orientation orientation) {
        if (orientation.isVertical()) {
            return new Position(line, index);
        }
        return new Position(index, line);
    }
}
