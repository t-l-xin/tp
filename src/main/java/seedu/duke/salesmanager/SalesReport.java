package seedu.duke.salesmanager;

import seedu.duke.salesmanager.exception.EmptyListException;
import seedu.duke.model.Item;
import seedu.duke.model.Shelf;
import seedu.duke.model.ShelfList;
import seedu.duke.model.exception.ShelfNotExistException;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.logging.Logger;

public class SalesReport {
    private final String selectedDate;
    private static final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    public static final String TOTAL_MONETARY_SUMMARY_MESSAGE_FORMAT = "Total Purchase Cost: %s\n"
            + "Total Selling Price: %s\nTotal Profits: %s";
    private static final String ITEM_INFO = "%o. %s (purchase cost: %s, selling price: %s)\n";
    private final String selectedEndDate;

    public SalesReport(String selectedDate, String selectedEndDate) {
        this.selectedDate = selectedDate;
        this.selectedEndDate = selectedEndDate;
    }

    public String generateSoldItemStats() throws EmptyListException {
        SalesManager salesManager = SalesManager.getSalesManager();
        ArrayList<SoldItem> selectedSoldItems = salesManager.filterSoldItems(selectedDate, selectedEndDate);
        BigDecimal totalPurchaseCost = BigDecimal.ZERO;
        BigDecimal totalSellingPrice = BigDecimal.ZERO;
        DecimalFormat decimalFormat = new DecimalFormat("0.00");

        for (int i = 0; i < selectedSoldItems.size(); i++) {
            Item selectedItem = selectedSoldItems.get(i);

            totalPurchaseCost = totalPurchaseCost.add(new BigDecimal(selectedItem.getPurchaseCost()));
            totalSellingPrice = totalSellingPrice.add(new BigDecimal(selectedItem.getSellingPrice()));
        }

        BigDecimal totalProfits = totalSellingPrice.subtract(totalPurchaseCost);

        System.out.println(String.format(TOTAL_MONETARY_SUMMARY_MESSAGE_FORMAT,
                decimalFormat.format(totalPurchaseCost), decimalFormat.format(totalSellingPrice),
                decimalFormat.format(totalProfits)));
        return String.format(TOTAL_MONETARY_SUMMARY_MESSAGE_FORMAT,
                decimalFormat.format(totalPurchaseCost), decimalFormat.format(totalSellingPrice),
                decimalFormat.format(totalProfits));
    }

    public String generateSoldItemDetails() {
        SalesManager salesManager = SalesManager.getSalesManager();
        ArrayList<SoldItem> selectedSoldItems = salesManager.filterSoldItems(selectedDate, selectedEndDate);
        StringBuilder info = new StringBuilder();
        for (int i = 0; i < selectedSoldItems.size();  i++) {
            Item selectedItem = selectedSoldItems.get(i);
            int index = i + 1;
            info.append(String.format(ITEM_INFO, index,
                    selectedItem.getName(), selectedItem.getPurchaseCost(), selectedItem.getSellingPrice()));
        } //todo: add remarks
        return info.toString().trim();
    }
}
