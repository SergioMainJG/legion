package legion.commands;

import java.util.Scanner;
import legion.battlefield.Battlefield;
import legion.battlefield.BattlefieldRenderer;
import legion.battlefield.Position;
import legion.console.ConsoleWriter;
import legion.errors.ExceptionHandler;
import legion.errors.types.InvalidCommandException;
import legion.troops.Troop;
import legion.troops.abilities.Attackable;
import legion.troops.abilities.Healable;

/**
 * Interactive session that reads commands until the user exits.
 * The loop intercepts failures outside the entry point so a bad command
 * never ends the session.
 */
public class GameLoop {

    private static final String PROMPT = "legion> ";
    private static final String TITLE = "Interactive session";
    private static final String ARGUMENT_SEPARATOR = "\\s+";
    private static final String NUMBER_PATTERN = "\\d+";
    private static final String FIELD_TITLE = "Battlefield";
    private static final String EXIT_MESSAGE = "Session closed.";
    private static final String MOVE_COMMAND = "move";
    private static final String ATTACK_COMMAND = "attack";
    private static final String HEAL_COMMAND = "heal";
    private static final String STATUS_COMMAND = "status";
    private static final String HELP_COMMAND = "help";
    private static final String EXIT_COMMAND = "exit";
    private static final int ACTOR_ARGUMENTS = 3;

    private final ConsoleWriter console;
    private final BattlefieldRenderer renderer;
    private final ExceptionHandler handler;
    private final Scanner scanner;
    private final Battlefield battlefield;

    /**
     * Creates the interactive session.
     *
     * @param console     output channel of the session
     * @param renderer    drawer of the battlefield
     * @param handler     centralized reporter of failures
     * @param scanner     input source of the session
     * @param battlefield matrix commanded by the user
     */
    public GameLoop(ConsoleWriter console, BattlefieldRenderer renderer, ExceptionHandler handler,
                    Scanner scanner, Battlefield battlefield) {
        this.console = console;
        this.renderer = renderer;
        this.handler = handler;
        this.scanner = scanner;
        this.battlefield = battlefield;
    }

    /**
     * Runs the session until the user writes the exit command.
     */
    public void run() {
        console.writeTitle(TITLE);
        printHelp();
        while (readAndExecute()) {
            console.writeLightSeparator();
        }
        console.writeLine(EXIT_MESSAGE);
        console.writeHeavySeparator();
    }

    private boolean readAndExecute() {
        console.writePrompt(PROMPT);
        if (!scanner.hasNextLine()) {
            return false;
        }
        String line = scanner.nextLine().trim();
        if (line.isBlank()) {
            return true;
        }
        return execute(line.split(ARGUMENT_SEPARATOR));
    }

    private boolean execute(String[] arguments) {
        try {
            return dispatch(arguments);
        } catch (RuntimeException failure) {
            handler.handle(failure);
            return true;
        }
    }

    private boolean dispatch(String[] arguments) {
        String command = arguments[0].toLowerCase();
        return switch (command) {
            case HELP_COMMAND -> keepRunningAfter(this::printHelp);
            case STATUS_COMMAND -> keepRunningAfter(() -> printStatus(arguments));
            case MOVE_COMMAND -> keepRunningAfter(() -> moveTroop(arguments));
            case ATTACK_COMMAND -> keepRunningAfter(() -> attackTroop(arguments));
            case HEAL_COMMAND -> keepRunningAfter(() -> healTroop(arguments));
            case EXIT_COMMAND -> false;
            default -> throw new InvalidCommandException("Unknown command: " + command
                    + ". Write help to list the available commands.");
        };
    }

    private boolean keepRunningAfter(Runnable action) {
        action.run();
        return true;
    }

    private void printHelp() {
        console.writeLine("Available commands");
        console.writeLabeled(MOVE_COMMAND, "move <id> <steps>, advances a troop with its own pattern");
        console.writeLabeled(ATTACK_COMMAND, "attack <id> <target>, offensive units only");
        console.writeLabeled(HEAL_COMMAND, "heal <id> <target>, support units only");
        console.writeLabeled(STATUS_COMMAND, "status, draws the field and lists every troop");
        console.writeLabeled(HELP_COMMAND, "help, prints this list");
        console.writeLabeled(EXIT_COMMAND, "exit, closes the session");
    }

    private void printStatus(String[] arguments) {
        if (arguments.length > 1) {
            console.writeLine(requireTroop(arguments[1]).getStatus());
            return;
        }
        renderer.render(battlefield, FIELD_TITLE);
        for (Troop troop : battlefield.deployedTroops()) {
            console.writeLine(troop.getStatus());
        }
    }

    private void moveTroop(String[] arguments) {
        requireArguments(arguments, MOVE_COMMAND, "move <id> <steps>");
        Troop troop = requireTroop(arguments[1]);
        Position origin = battlefield.locate(troop.getIdentifier());
        Position destination = troop.moveFrom(origin, requireNumber(arguments[2]));
        if (destination.equals(origin)) {
            console.writeLine(troop.getIdentifier() + " held its position at " + origin);
            return;
        }
        validateDestination(destination);
        battlefield.release(origin);
        battlefield.place(destination, troop);
        console.writeLine(troop.getIdentifier() + " moved from " + origin + " to " + destination);
    }

    private void validateDestination(Position destination) {
        if (!battlefield.contains(destination)) {
            throw new InvalidCommandException("Destination " + destination + " is outside the battlefield.");
        }
        if (battlefield.isOccupied(destination)) {
            throw new InvalidCommandException("Destination " + destination + " is already occupied.");
        }
    }

    private void attackTroop(String[] arguments) {
        requireArguments(arguments, ATTACK_COMMAND, "attack <id> <target>");
        Troop actor = requireTroop(arguments[1]);
        Troop target = requireTroop(arguments[2]);
        if (!(actor instanceof Attackable)) {
            throw new InvalidCommandException(actor.getIdentifier() + " cannot attack.");
        }
        console.writeLine("Action executed: " + actor.getIdentifier()
                + " attacks " + target.getIdentifier());
    }

    private void healTroop(String[] arguments) {
        requireArguments(arguments, HEAL_COMMAND, "heal <id> <target>");
        Troop actor = requireTroop(arguments[1]);
        Troop target = requireTroop(arguments[2]);
        if (!(actor instanceof Healable)) {
            throw new InvalidCommandException(actor.getIdentifier() + " cannot heal.");
        }
        console.writeLine("Action executed: " + actor.getIdentifier()
                + " heals " + target.getIdentifier());
    }

    private void requireArguments(String[] arguments, String command, String usage) {
        if (arguments.length < ACTOR_ARGUMENTS) {
            throw new InvalidCommandException("Command " + command + " expects the form " + usage + ".");
        }
    }

    private Troop requireTroop(String identifier) {
        Position position = battlefield.locate(identifier);
        if (position == null) {
            throw new InvalidCommandException("There is no troop with identifier " + identifier + ".");
        }
        return battlefield.troopAt(position);
    }

    private int requireNumber(String value) {
        if (!value.matches(NUMBER_PATTERN)) {
            throw new InvalidCommandException("The amount of steps must be a whole number, received " + value + ".");
        }
        return Integer.parseInt(value);
    }
}
