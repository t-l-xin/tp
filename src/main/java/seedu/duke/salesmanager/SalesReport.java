package seedu.duke.salesmanager;

import seedu.duke.command.exception.IllegalArgumentException;
import seedu.duke.model.Item;
import seedu.duke.model.SoldItem;
import seedu.duke.salesmanager.exception.EmptyListException;
import seedu.duke.ui.Wrapping;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.logging.Logger;

public class SalesReport {
    public static final BigDecimal ONE_HUNDRED = new BigDecimal(100);
    public static final String TOTAL_MONETARY_SUMMARY_MESSAGE_FORMAT = "Total Purchase Cost: $ %s\n"
            + "Total Selling Price: $ %s\nTotal Profits: $ %s\nGross Profit Margin (in percent): %s";
    private static final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private static final String HEADER =
            "  No  |                    Item                 |  Cost   |  Price  |  Profit |      Sold Time    \n";
    private static final String BORDER =
            "-------------------------------------------------------------------------------------------------\n";
    private static final int INDEX_TABLE_LENGTH = 4;
    private static final int ITEM_TABLE_LENGTH = 40;
    private static final int COST_TABLE_LENGTH = 8;
    private static final int PRICE_TABLE_LENGTH = 8;
    private static final int PROFIT_TABLE_LENGTH = 8;
    private static final String ITEM_INFO = "%s| %s| %s| %s| %s|%s\n";
    private final String selectedDate;
    private final String selectedEndDate;

    public SalesReport(String selectedDate, String selectedEndDate) {
        this.selectedDate = selectedDate;
        this.selectedEndDate = selectedEndDate;
    }

    /**
     * Generate a string to contain all sold item statistics.
     *
     * @return A String containing the sold items statistics
     * @throws EmptyListException If the soldItems shelf does not contain items
     */
    public String generateSoldItemStats()
            throws EmptyListException, IllegalArgumentException {
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
        BigDecimal grossProfitMargin = totalProfits.divide(totalSellingPrice, 2,
                RoundingMode.HALF_UP).multiply(ONE_HUNDRED);

        return String.format(TOTAL_MONETARY_SUMMARY_MESSAGE_FORMAT,
                decimalFormat.format(totalPurchaseCost), decimalFormat.format(totalSellingPrice),
                decimalFormat.format(totalProfits), decimalFormat.format(grossProfitMargin));
    }

    /**
     * Generate and gets every sold item details in the soldItems shelf.
     *
     * @return A String containing all sold item details
     */
    public String generateSoldItemDetails() throws IllegalArgumentException {
        SalesManager salesManager = SalesManager.getSalesManager();
        ArrayList<SoldItem> selectedSoldItems = salesManager.filterSoldItems(selectedDate, selectedEndDate);
        StringBuilder details = new StringBuilder();
        details.append(HEADER);
        details.append(BORDER);
        for (int i = 0; i < selectedSoldItems.size(); i++) {
            SoldItem selectedSoldItem = selectedSoldItems.get(i);
            int index = i + 1;
            String indexAsString = Wrapping.restrictMessageLength(Integer.toString(index), INDEX_TABLE_LENGTH);
            String name = selectedSoldItem.getName();
            name = Wrapping.restrictMessageLength(name, ITEM_TABLE_LENGTH);
            String cost = selectedSoldItem.getPurchaseCost();
            String wrappedCost = Wrapping.restrictMessageLength(cost, COST_TABLE_LENGTH);
            String price = selectedSoldItem.getSellingPrice();
            String wrappedPrice = Wrapping.restrictMessageLength(price, PRICE_TABLE_LENGTH);
            BigDecimal profit = new BigDecimal(price).subtract(new BigDecimal(cost));
            String profitAsString = profit.toString();
            profitAsString = Wrapping.restrictMessageLength(profitAsString, PROFIT_TABLE_LENGTH);
            DateTimeFormatter saleTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String saleTime = selectedSoldItem.getSaleTime().format(saleTimeFormatter);
            details.append(String.format(ITEM_INFO, indexAsString, name,
                    wrappedCost, wrappedPrice, profitAsString, saleTime));
        } //todo: add remarks
        return details.toString().trim();
    }
}
