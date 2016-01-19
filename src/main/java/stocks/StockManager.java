package stocks;

import stocks.model.Stock;
import stocks.model.Trade;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by allemaos on 15/01/16.
 */
public interface StockManager {

    /**
     * Record in the Stock Manager a trade represented by the object <i>trade</i>.
     *
     * @param trade The trade object to record.
     *
     * @return True when the operation of record a trade is successful. Other case, False.
     *
     * @throws Exception A exception during the operation of record a trade.
     */
    public boolean recordTrade(Trade trade) throws Exception;

    /**
     * Loads Stocks and trades.
     *
     *  @return True when the operation of record a trade is successful. Other case, False.
     */
    public boolean load();
    /**
     * Gets the array list that contains all the trades.
     *
     * @return The array list that contains all the trades.
     */
    public ArrayList<Trade> getTrades();

    /**
     *
     * @param symbol
     * @return
     */
    public Stock getStockBySymbol(String symbol);

    /**
     * Gets all the stocks.
     *
     * @return The hash map that contains all the stocks.
     */
    public HashMap<String, Stock> getStocks();
}
