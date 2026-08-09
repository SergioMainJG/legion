package legion.battlefield;

import legion.console.ConsoleWriter;
import legion.troops.Troop;
import legion.troops.TroopType;

/**
 * Drawing of the battlefield matrix on the console.
 * The renderer only reads the matrix, it never changes its content.
 */
public class BattlefieldRenderer {

    private static final String EMPTY_CELL = "*";
    private static final String CELL_SEPARATOR = " ";
    private static final String LEGEND_TITLE = "Legend";
    private static final String LEGEND_SEPARATOR = " = ";
    private static final String EMPTY_LEGEND = EMPTY_CELL + LEGEND_SEPARATOR + "empty cell";

    private final ConsoleWriter console;

    /**
     * Creates the renderer with the output channel it must use.
     *
     * @param console writer used to draw the matrix
     */
    public BattlefieldRenderer(ConsoleWriter console) {
        this.console = console;
    }

    /**
     * Draws a titled battlefield followed by its legend.
     *
     * @param battlefield matrix to draw
     * @param title       heading shown above the matrix
     */
    public void render(Battlefield battlefield, String title) {
        console.writeTitle(title);
        drawRows(battlefield);
        console.writeLightSeparator();
        drawLegend();
        console.writeHeavySeparator();
    }

    private void drawRows(Battlefield battlefield) {
        for (int row = 0; row < battlefield.getSize(); row++) {
            console.writeLine(buildRow(battlefield, row));
        }
    }

    private String buildRow(Battlefield battlefield, int row) {
        StringBuilder line = new StringBuilder();
        for (int column = 0; column < battlefield.getSize(); column++) {
            line.append(symbolAt(battlefield, new Position(column, row)));
            line.append(CELL_SEPARATOR);
        }
        return line.toString().trim();
    }

    private String symbolAt(Battlefield battlefield, Position position) {
        Troop troop = battlefield.troopAt(position);
        if (troop == null) {
            return EMPTY_CELL;
        }
        return troop.getType().getSymbol();
    }

    private void drawLegend() {
        console.writeLine(LEGEND_TITLE);
        for (TroopType type : TroopType.implementedValues()) {
            console.writeLine(type.getSymbol() + LEGEND_SEPARATOR + type.getLabel());
        }
        console.writeLine(EMPTY_LEGEND);
    }
}
