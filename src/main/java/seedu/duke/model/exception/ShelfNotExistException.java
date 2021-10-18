package seedu.duke.model.exception;

public class ShelfNotExistException extends Exception {

    public static final String MESSAGE_ITEM_NOT_EXIST = "The item container %s does not exist";

    public ShelfNotExistException(String itemContainerName) {
        super(String.format(MESSAGE_ITEM_NOT_EXIST, itemContainerName));
    }

}