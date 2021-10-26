package seedu.duke.storage;

import org.json.JSONObject;
import seedu.duke.model.Item;
import seedu.duke.model.Shelf;
import seedu.duke.model.ShelfList;
import seedu.duke.model.exception.DuplicateItemException;
import seedu.duke.model.exception.DuplicateShelfException;
import seedu.duke.model.exception.IllegalArgumentException;
import seedu.duke.model.exception.ShelfNotExistException;
import seedu.duke.salesmanager.SoldItem;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;

public class Storage {

    public static String STORAGE_PATH = "data\\Data.txt";

    private static final int JSON_INDENTATION_LEVEL = 4;

    ShelfList shelfList;

    public Storage() {
        shelfList = ShelfList.getShelfList();
    }

    public void saveData() throws ShelfNotExistException, IOException {
        String nameOfAllShelves = shelfList.getAllShelvesName();
        JSONObject storedData = new JSONObject();

        if (nameOfAllShelves.isBlank()) {
            storedData = sampleData();
        } else {
            for (String nameOfShelf : nameOfAllShelves.split("\n")) {
                Shelf currentShelf = shelfList.getShelf(nameOfShelf);
                JSONObject shelfInfo = new JSONObject();

                shelfInfo.put("remark", currentShelf.getRemark());

                JSONObject itemsInShelf = new JSONObject();
                for (int i = 0; i < currentShelf.getSize(); i = i + 1) {
                    Item currentItem = currentShelf.getItem(i);
                    JSONObject itemDetail = new JSONObject();
                    itemDetail.put("name", currentItem.getName());
                    itemDetail.put("cost", currentItem.getPurchaseCost());
                    itemDetail.put("price", currentItem.getSellingPrice());
                    itemDetail.put("remark", currentItem.getRemark());
                    if (currentShelf.getName() == "soldItems") {
                        itemDetail.put("saleTime", ((SoldItem) currentItem).getSaleTime());
                    }
                    itemsInShelf.put(Integer.toString(i), itemDetail);
                }
                shelfInfo.put("items", itemsInShelf);
                storedData.put(currentShelf.getName(), shelfInfo);
            }
        }
        File file = new File(STORAGE_PATH);
        if (!file.exists()) {
            String directory = STORAGE_PATH.substring(0, STORAGE_PATH.lastIndexOf("\\"));
            Files.createDirectories(Paths.get(directory));
            file.createNewFile();
        }
        FileWriter writer = new FileWriter(STORAGE_PATH);
        writer.write(storedData.toString(JSON_INDENTATION_LEVEL));
        writer.close();
    }

    public void loadData()
            throws IOException, DuplicateShelfException, IllegalArgumentException, DuplicateItemException {
        File file = new File(STORAGE_PATH);
        if (!file.exists()) {
            String directory = STORAGE_PATH.substring(0, STORAGE_PATH.lastIndexOf("\\"));
            Files.createDirectories(Paths.get(directory));
            file.createNewFile();

            FileWriter writer = new FileWriter(STORAGE_PATH);
            writer.write(sampleData().toString(JSON_INDENTATION_LEVEL));
            writer.close();
        }
        String text = Files.readString(Paths.get(STORAGE_PATH));
        try {
            JSONObject storedData = new JSONObject(text);
            loadFromJson(storedData);
        } catch (Exception e) {
            shelfList.resetShelfList();
            loadFromJson(sampleData());
        }

    }

    private void loadFromJson(JSONObject storedData)
            throws DuplicateShelfException, IllegalArgumentException, DuplicateItemException {
        for (String shelfName : storedData.keySet()) {
            Shelf currentShelf = shelfList.addShelf(shelfName);
            currentShelf.setRemark(storedData.getJSONObject(shelfName).getString("remark"));
            JSONObject itemsJson = storedData.getJSONObject(shelfName).getJSONObject("items");
            for (String itemName : itemsJson.keySet()) {
                JSONObject itemJson = itemsJson.getJSONObject(itemName);
                Item item;
                if (shelfName != "soldItems") {
                    item = new Item(
                            itemJson.get("name").toString(),
                            itemJson.get("cost").toString(),
                            itemJson.get("price").toString()
                    );
                } else {
                    item = new SoldItem(
                            itemJson.get("name").toString(),
                            itemJson.get("cost").toString(),
                            itemJson.get("price").toString(),
                            LocalDateTime.parse(itemJson.get("saleTime").toString())
                    );
                }
                item.setRemark(itemJson.getString("remark"));
                currentShelf.addItem(item);
            }
        }
    }

    private JSONObject sampleData() {
        final JSONObject defaultShelves = new JSONObject();
        final JSONObject defaultWarehouse = new JSONObject();
        final JSONObject defaultItems = new JSONObject();
        JSONObject sampleItem = new JSONObject();
        sampleItem.put("name", "sample item");
        sampleItem.put("cost", "12.25");
        sampleItem.put("price", "25");
        sampleItem.put("remark", " ");
        defaultItems.put("0", sampleItem);
        defaultWarehouse.put("items", defaultItems);
        defaultWarehouse.put("remark", " ");
        defaultShelves.put("warehouse", defaultWarehouse);
        return defaultShelves;
    }
}