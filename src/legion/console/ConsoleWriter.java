package legion.console;

/**
 * Single output channel of the application.
 * Every message printed by the program passes through this class so
 * the console keeps a uniform width and a uniform separator style.
 */
public class ConsoleWriter {

    private static final int WIDTH = 62;
    private static final String LIGHT_SEPARATOR_UNIT = "-";
    private static final String HEAVY_SEPARATOR_UNIT = "=";
    private static final String LABEL_SEPARATOR = ": ";

    /**
     * Writes a plain line of text.
     *
     * @param text content to print
     */
    public void writeLine(String text) {
        System.out.println(text);
    }

    /**
     * Writes an empty line.
     */
    public void writeBlankLine() {
        System.out.println();
    }

    /**
     * Writes a light separator used between sections of the same block.
     */
    public void writeLightSeparator() {
        System.out.println(LIGHT_SEPARATOR_UNIT.repeat(WIDTH));
    }

    /**
     * Writes a heavy separator used to open and close major blocks.
     */
    public void writeHeavySeparator() {
        System.out.println(HEAVY_SEPARATOR_UNIT.repeat(WIDTH));
    }

    /**
     * Writes a title surrounded by heavy separators.
     *
     * @param title text of the title
     */
    public void writeTitle(String title) {
        writeBlankLine();
        writeHeavySeparator();
        writeLine(title.toUpperCase());
        writeHeavySeparator();
    }

    /**
     * Writes an inline prompt without moving to the next line.
     *
     * @param prompt text shown before the caret
     */
    public void writePrompt(String prompt) {
        System.out.print(prompt);
    }

    /**
     * Writes a labelled value using the standard label separator.
     *
     * @param label name of the value
     * @param value content of the value
     */
    public void writeLabeled(String label, String value) {
        writeLine(label + LABEL_SEPARATOR + value);
    }

    /**
     * Writes a failure block with its code and message.
     *
     * @param code    identifier of the failure
     * @param message description of the failure
     */
    public void writeFailure(String code, String message) {
        writeHeavySeparator();
        writeLine("ERROR " + code);
        writeLine(message);
        writeHeavySeparator();
    }
}
