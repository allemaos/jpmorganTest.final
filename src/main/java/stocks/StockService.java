package stocks;

import stocks.model.Trade;

/**
 * Created by allemaos on 15/01/16.
 */
public interface StockService {
    public double calculateDividendYield(String symbol) throws Exception;

    /**
     * Calculates the P/E Ratio.
     *
     * @param symbol Stock symbol.
     *
     * @return A double value which represents the P/E Ratio.
     *
     * @throws Exception A exception raised during the execution of the method due to an error.
     */
    public double calculatePERatio(String symbol) throws Exception;

    /**
     * Record a trade.
     *
     * @param trade Trade object to record.
     *
     * @return True, when the record is successful. Other case, False.
     */
    public boolean recordTrade(Trade trade) throws Exception;

    /**
     *
     * @param symbol
     * @return
     * @throws Exception
     */
    public double calculateStockPrice(String symbol) throws Exception;

    /**
     * Calculates the GBCE All Share Index.
     *
     * @return
     * @throws Exception
     */
    public double calculateGBCEAllShareIndex() throws Exception;
}
