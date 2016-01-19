package stocks;

import org.apache.log4j.Logger;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.math3.stat.StatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import stocks.model.Stock;
import stocks.model.Trade;

import java.util.*;

@Service
public class StockServiceImpl implements StockService {

    private Logger logger = Logger.getLogger(StockServiceImpl.class);

    @Autowired
    private StockManager stockManager;

    @Override
    public double calculateDividendYield(String symbol) throws Exception{
        double dividendYield = -1.0;

        try{
            logger.debug("Calculating Dividend Yield for the stock symbol: "+symbol);
            Stock stock = stockManager.getStockBySymbol(symbol);

            if(stock==null){
                throw new Exception("The stock symbol ["+symbol+"] is not supported by the Super Simple Stock system.");
            }

            if(stock.getTickerPrice() <= 0.0){
                logger.debug("The ticker price for the stock ["+symbol+"] should be greater than zero (0).");
            }
            dividendYield = stock.getDividendYield();

            logger.debug("Dividend Yield calculated: "+dividendYield);

        }catch(Exception exception){
            logger.error("Error calculating Dividend Yield for the stock symbol: "+symbol+".", exception);
            throw new Exception("Error calculating Dividend Yield for the stock symbol: "+symbol+".", exception);
        }
        return dividendYield;
    }

    @Override
    public double calculatePERatio(String symbol) throws Exception{
        double peRatio = -1.0;
        try{
            logger.debug("Calculating P/E Ratio for the stock symbol: "+symbol);
            Stock stock = stockManager.getStockBySymbol(symbol);

            // If the stock is not supported the a exception is raised
            if(stock==null){
                throw new Exception("The stock symbol ["+symbol+"] is not supported by the Super Simple Stock system.");
            }

            // Ticker price with value zero does not make any sense and could produce a zero division
            if(stock.getTickerPrice() <= 0.0){
                throw new Exception("The ticker price for the stock ["+symbol+"] should be greater than zero (0).");
            }

            peRatio = stock.getPERatio();

            logger.debug(" P/E Ratiocalculated: "+peRatio);

        }catch(Exception exception){
            logger.error("Error calculating P/E Ratio for the stock symbol: "+symbol+".", exception);
            throw new Exception("Error calculating P/E Ratio for the stock symbol: "+symbol+".", exception);
        }
        return peRatio;
    }

    @Override
    public boolean recordTrade(Trade trade) throws Exception{
        boolean recordResult = false;
        try{
            logger.debug("Begin recordTrade with trade object: ");
            logger.debug(trade);

            // trade should be an object
            if(trade==null){
                throw new Exception("Trade object to record should be a valid object and it's null.");
            }

            // stock should be an object
            if(trade.getStock()==null){
                throw new Exception("A trade should be associated with a stock and the stock for the trade is null.");
            }

            // shares quantity should be greater than zero
            if(trade.getShares()<=0){
                throw new Exception("Shares quantity in the trade to record should be greater than cero.");
            }

            // shares price should be greater than zero
            if(trade.getPrice()<=0.0){
                throw new Exception("Shares price in the trade to record should be greater than cero.");
            }

            recordResult = stockManager.recordTrade(trade);

            // Update the ticker price for the stock
            if(recordResult){
                trade.getStock().setTickerPrice(trade.getPrice());
            }


        }catch(Exception exception){
            logger.error("Error when trying to record a trade.", exception);
            throw new Exception("Error when trying to record a trade.", exception);
        }
        return recordResult;
    }

    /**
     *
     * @param symbol
     * @param minutesRange
     * @return
     * @throws Exception
     */
    private double calculateStockPriceinRange(String symbol, int minutesRange) throws Exception{
        double stockPrice = 0.0;

        logger.debug("Trades in the original collection: "+getTradesNumber());


        @SuppressWarnings("unchecked")
        Collection<Trade> trades = CollectionUtils.select(stockManager.getTrades(), new StockPredicate(symbol, minutesRange));

        logger.debug("Trades in the filtered collection by ["+symbol+","+minutesRange+"]: "+trades.size());

        // Calculate the summation
        double shareQuantityAcum = 0.0;
        double tradePriceAcum = 0.0;
        for(Trade trade : trades){
            // Calculate the summation of Trade Price x Quantity
            tradePriceAcum += (trade.getPrice() * trade.getShares());
            // Acumulate Quantity
            shareQuantityAcum += trade.getShares();
        }

        // calculate the stock price
        if(shareQuantityAcum > 0.0){
            stockPrice = tradePriceAcum / shareQuantityAcum;
        }


        return stockPrice;
    }

    @Override
    public double calculateStockPrice(String symbol) throws Exception{
        double stockPrice = 0.0;

        try{
            logger.debug("Calculating Stock Price for the stock symbol: "+symbol);
            Stock stock = stockManager.getStockBySymbol(symbol);

            // If the stock is not supported the a exception is raised
            if(stock==null){
                throw new Exception("The stock symbol ["+symbol+"] is not supported by the Super Simple Stock system.");
            }

            stockPrice = calculateStockPriceinRange(symbol, 15);

            logger.debug(" Stock Price calculated: "+stockPrice);


        }catch(Exception exception){
            logger.error("Error calculating P/E Ratio for the stock symbol: "+symbol+".", exception);
            throw new Exception("Error calculating P/E Ratio for the stock symbol: "+symbol+".", exception);

        }


        return stockPrice;
    }

    @Override
    public double calculateGBCEAllShareIndex() throws Exception{
        double allShareIndex = 0.0;

        // Calculate stock price for all stock in the system
        HashMap<String, Stock> stocks = stockManager.getStocks();
        ArrayList<Double> stockPrices = new ArrayList<Double>();
        for(String symbol: stocks.keySet() ){
            if(stocks.get(symbol).getLastDividend()!=0) {
                double stockPrice = calculateStockPriceinRange(symbol, 0);
                if (stockPrice > 0) {
                    stockPrices.add(stockPrice);
                }
            }
        }

        if(stockPrices.size()>=1){
            double[] stockPricesArray = new double[stockPrices.size()];

            for(int i=0; i<=(stockPrices.size()-1); i++){
                stockPricesArray[i] = stockPrices.get(i).doubleValue();
            }
            // Calculates the GBCE All Share Index
            allShareIndex = StatUtils.geometricMean(stockPricesArray);
        }
        return allShareIndex;
    }

    public int getTradesNumber() throws Exception {
        return stockManager.getTrades().size();
    }

    private class StockPredicate implements Predicate{
        /**
         *
         */
        private Logger logger = Logger.getLogger(StockPredicate.class);

        /**
         *
         */
        private String symbol = "";

        /**
         *
         */
        private Calendar dateRange = null;

        /**
         *
         * @param symbol
         * @param minutesRange
         */
        public StockPredicate(String symbol, int minutesRange){
            this.symbol = symbol;
            if( minutesRange > 0 ){
                dateRange = Calendar.getInstance();
                dateRange.add(Calendar.MINUTE, -minutesRange);
                logger.debug(String.format("Filter date pivot: %tF %tT", dateRange.getTime(), dateRange.getTime()));
            }

        }

        /**
         *
         */
        public boolean evaluate(Object tradeObject) {
            Trade trade = (Trade) tradeObject;
            boolean shouldBeInclude = trade.getStock().getSymbol().equals(symbol);
            if(shouldBeInclude && dateRange != null){
                shouldBeInclude = dateRange.getTime().compareTo(new Date(trade.getTimestamp()))<=0;
            }
            return shouldBeInclude;
        }

    }

}
