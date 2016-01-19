package stocks;

import stocks.model.Stock;
import stocks.model.Trade;

import java.util.ArrayList;

/**
 * Created by allemaos on 16/01/16.
 */
public interface StockRepository {

    /**
     * Set a stock for a given symbol or get the cached stock.
     *
     * @param symbol The stock symbol.
     *
     * @return The stock.
     */
    Stock getBySymbol(String symbol);
    /**
     * Set a trade for a given timestamp or get the cached rtade.
     *
     * @param timestamp The trade timestamp.
     *
     * @return The stock.
     */
    Trade getByTimestamp(long timestamp);
    /**
     * Gets all trades' timestamps.
     *
     * @return The array list that contains all trades' timestamps.
     */
    ArrayList<Long> getAllTradeTimestamps();

    /**
     * Gets all stocks' symbols.
     *
     * @return The array list that contains all stocks' symbols.
     */    ArrayList<String> getAllStockSymbols();

}
