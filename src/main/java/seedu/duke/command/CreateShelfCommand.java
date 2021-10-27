package seedu.duke.command;

import seedu.duke.command.exception.CommandException;
import seedu.duke.command.exception.DuplicateShelfException;
import seedu.duke.command.exception.IllegalArgumentException;
import seedu.duke.model.ShelfList;

public class CreateShelfCommand extends Command {
    private static final String CREATE_COMPLETE_MESSAGE =
            "This shelf has been created.";
    private final String shelfName;

    public CreateShelfCommand(String shelfName) {
        this.shelfName = shelfName;
    }

    @Override
    public String execute() throws CommandException {
        try {
            ShelfList.getShelfList().addShelf(shelfName);
            return CREATE_COMPLETE_MESSAGE;
        } catch (seedu.duke.model.exception.DuplicateShelfException e) {
            throw new DuplicateShelfException(e.getMessage());
        } catch (seedu.duke.model.exception.IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}