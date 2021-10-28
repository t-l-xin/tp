package seedu.duke;

import seedu.duke.command.Command;
import seedu.duke.model.Shelf;
import seedu.duke.model.ShelfList;
import seedu.duke.parser.Parser;
import seedu.duke.storage.DataCorruptionException;
import seedu.duke.storage.Storage;
import seedu.duke.ui.DukePredefinedMessages;
import seedu.duke.ui.MessageBubble;

import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CliverShelf {
    private static final String HELP_PROMPT_MESSAGE = "Enter 'help' for the list of available commands";

    /**
     * Main entry-point for the java.duke.Duke application.
     */
    public static void main(String[] args) throws Exception {
        Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
        logger.setLevel(Level.WARNING);

        Storage storage = Storage.getStorageManager();
        try {
            storage.loadData();
        } catch (DataCorruptionException e) {
            MessageBubble.printMessageBubble("Database was corrupted");
        }

        Scanner in = new Scanner(System.in);
        DukePredefinedMessages.printWelcomeMessage();
        MessageBubble.printMessageBubble(HELP_PROMPT_MESSAGE);
        String input;
        Parser parser = new Parser();
        boolean isExit = false;
        while (!isExit) {
            input = in.nextLine();
            try {
                Command command = parser.parseCommand(input);
                String resultString = command.execute();
                isExit = command.isExit();
                MessageBubble.printMessageBubble(resultString);
            } catch (Exception e) {
                MessageBubble.printMessageBubble(e.getMessage());
            }
            storage.saveData();
        }
    }
}