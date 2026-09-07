package legion.battlefield;

import legion.console.ConsoleWriter;
import legion.troops.Troop;
import legion.troops.TroopType;

/**
 * Drawing of the battlefield matrix on the console.
 * The renderer only reads the matrix, it never changes its content.
 * Row and column indices are printed so the orientation of the final
 * formation can be read directly from the map.
 */
public class BattlefieldRenderer {

    private static final String EMPTY_CELL = "*";
    private static final String CELL_SEPARATOR = " ";
    private static final String CORNER = "      ";
    private static final String ROW_LABEL_SEPARATOR = " | ";
    private static final String LEGEND_TITLE = "Legend";
    private static final String LEGEND_SEPARATOR = " = ";
    private static final String EMPTY_LEGEND = EMPTY_CELL + LEGEND_SEPARATOR + "empty cell";
    private static final int LABEL_WIDTH = 3;

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
        console.writeLine(buildColumnHeader(battlefield));
        drawRows(battlefield);
        console.writeLightSeparator();
        drawLegend();
        console.writeHeavySeparator();
    }

    private String buildColumnHeader(Battlefield battlefield) {
        StringBuilder header = new StringBuilder(CORNER);
        for (int column = 0; column < battlefield.getSize(); column++) {
            header.append(pad(column)).append(CELL_SEPARATOR);
        }
        return header.toString().stripTrailing();
    }

    private void drawRows(Battlefield battlefield) {
        for (int row = 0; row < battlefield.getSize(); row++) {
            console.writeLine(pad(row) + ROW_LABEL_SEPARATOR + buildRow(battlefield, row));
        }
    }

    private String buildRow(Battlefield battlefield, int row) {
        StringBuilder line = new StringBuilder();
        for (int column = 0; column < battlefield.getSize(); column++) {
            line.append(pad(symbolAt(battlefield, new Position(column, row))));
            line.append(CELL_SEPARATOR);
        }
        return line.toString().stripTrailing();
    }

    private String symbolAt(Battlefield battlefield, Position position) {
        Troop troop = battlefield.troopAt(position);
        if (troop == null) {
            return EMPTY_CELL;
        }
        return troop.getType().getSymbol();
    }

    private String pad(int value) {
        return pad(String.valueOf(value));
    }

    private String pad(String text) {
        if (text.length() >= LABEL_WIDTH) {
            return text;
        }
        return " ".repeat(LABEL_WIDTH - text.length()) + text;
    }

    private void drawLegend() {
        console.writeLine(LEGEND_TITLE);
        for (TroopType type : TroopType.deploymentOrder()) {
            console.writeLine(type.getSymbol() + LEGEND_SEPARATOR + type.getLabel());
        }
        console.writeLine(EMPTY_LEGEND);
    }
}
