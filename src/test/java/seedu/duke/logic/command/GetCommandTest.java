package seedu.duke.logic.command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seedu.duke.logic.command.exception.ItemNotExistCommandException;
import seedu.duke.model.Item;
import seedu.duke.model.Shelf;
import seedu.duke.model.ShelfList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GetCommandTest {

    private Shelf testList;
    private Command testCommand1;
    private Command testCommand2;
    public static final String ITEM_DESCRIPTION = "Name: %s\nCost: %s\nPrice: %s\nRemarks: %s";
    public static final String GET_COMPLETE_MESSAGE = "Here is the information of your item:\n";

    @BeforeEach
    public void setUp() throws Exception {
        ShelfList.getShelfList().resetShelfList();
        testList = new Shelf("test");
        testCommand1 = new GetCommand("test", "1");
        testCommand2 = new GetCommand("test", "3");
    }


    @Test
    public void execute_ItemInList_getsNormally() throws Exception {
        Item.setPrintDummyID(true);
        testList.addItem(new Item("HarryPotter", "16.1", "25.12", "Hello", Item.DUMMY_ID));
        assertTrue(testList.contains("HarryPotter"));
        String expected = String.format(GetCommand.GET_COMPLETE_MESSAGE + GetCommand.GET_OUTPUT,
                "HarryPotter", "16.1", "25.12", Item.DUMMY_ID, "Hello");
        assertEquals(expected, testCommand1.execute());
    }

    @Test
    public void execute_emptyList_throwsItemNotExistException() {
        assertThrows(ItemNotExistCommandException.class, () -> testCommand2.execute());
    }

    @Test
    public void execute_noMatchedItemInList_throwItemNotExistException() throws Exception {
        testList.addItem(new Item("Hello", "25.12", "16.1", ""));
        assertTrue(testList.contains("Hello"));
        assertThrows(ItemNotExistCommandException.class, () -> testCommand2.execute());
    }
}