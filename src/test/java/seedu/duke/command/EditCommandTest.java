package seedu.duke.command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import seedu.duke.model.ContainerList;
import seedu.duke.model.Item;
import seedu.duke.model.ItemContainer;
import seedu.duke.command.exception.ItemNotExistException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EditCommandTest {

    private ItemContainer testList;
    private Command testCommand;

    @BeforeEach
    public void setUp() throws Exception {
        ContainerList.getContainerList().resetContainerList();
        testList = new ItemContainer("test");
    }

    @Test
    public void execute_oneItemAlreadyInList_editsNormally() throws Exception {
        testList.addItem(new Item("HarryPotter", "16.1", "25.12"));
        testCommand = new EditCommand("HarryPotter", "16.1", "20");
        assertTrue(testList.contains("HarryPotter"));
        assertEquals("25.12", testList.getItem("HarryPotter").getSellingPrice());
        int numberOfItemsBeforeEditing = testList.getSize();
        testCommand.execute(testList);
        int numberOfItemAfterEditing = testList.getSize();
        assertEquals(numberOfItemAfterEditing, numberOfItemsBeforeEditing);
        assertTrue(testList.contains("HarryPotter"));
        assertEquals("20", testList.getItem("HarryPotter").getSellingPrice());
    }

    @Test
    public void execute_emptyList_throwsItemNotExistException() {
        testCommand = new EditCommand("HarryPotter", "16.1", "20");
        assertThrows(ItemNotExistException.class, () -> testCommand.execute(testList));
    }

    @Test
    public void execute_noMatchedItemInList_throwItemNotExistException() throws Exception {
        testList.addItem(new Item("HarryPotter", "16.1", "25.12"));
        testCommand = new EditCommand("HarryPotter2", "16.1", "20");
        assertThrows(ItemNotExistException.class, () -> testCommand.execute(testList));
    }

}
