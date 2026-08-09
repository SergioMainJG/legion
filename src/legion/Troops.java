package legion;

import java.util.Scanner;
import legion.console.ConsoleWriter;
import legion.errors.ExceptionHandler;

/**
 * Entry point of the simulator.
 * It intercepts every failure of the startup path so all of them are
 * reported through the centralized handler.
 */
public final class Troops {

    private Troops() {
    }

    /**
     * Starts the simulator.
     *
     * @param arguments key value pairs of the form key=value
     */
    public static void main(String[] arguments) {
        ConsoleWriter console = new ConsoleWriter();
        ExceptionHandler handler = new ExceptionHandler(console);
        Scanner scanner = new Scanner(System.in);
        try {
            new LegionApplication(console, handler, scanner).run(arguments);
        } catch (RuntimeException failure) {
            handler.handle(failure);
        }
    }
}
