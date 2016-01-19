package stocks.model;

import org.apache.log4j.Logger;

/**
 * Created by allemaos on 15/01/16.
 */
public class Stock {

    private Logger logger = Logger.getLogger(Stock.class);

    private String symbol;
    private double lastDividend;
    private double fixedDividend;
    private double parValue;
    private double tickerPrice;
    private StockType stockType;

    /**
     *
     */
    public Stock(){}
    public Stock(String symbol){
        this.symbol = symbol;
    }

    public Stock loadStock(
                 double lastDividend,
                 double fixedDividend,
                 double parValue,
                 StockType stockType){
        this.lastDividend = lastDividend;
        this.fixedDividend = fixedDividend;
        this.parValue = parValue;
        this.stockType = stockType;
        return this;
    }

    public double getDividendYield() {
        double dividendYield = -1.0;
        if(tickerPrice > 0.0){
            if( stockType==StockType.COMMON){
                dividendYield = lastDividend / tickerPrice;
            }else{
                dividendYield = (fixedDividend * parValue ) / tickerPrice;
            }
        }
        return dividendYield;
    }

    public double getPERatio() {
        double peRatio = -1.0;

        if(tickerPrice > 0.0){
            peRatio = tickerPrice / getDividendYield();
        }

        return peRatio;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public double getLastDividend() {
        return lastDividend;
    }

    public void setLastDividend(double lastDividend) {
        this.lastDividend = lastDividend;
    }

    public double getFixedDividend() {
        return fixedDividend;
    }

    public void setFixedDividend(double fixedDividend) {
        this.fixedDividend = fixedDividend;
    }

    public double getParValue() {
        return parValue;
    }

    public void setParValue(double parValue) {
        this.parValue = parValue;
    }

    public double getTickerPrice() {
        return tickerPrice;
    }

    public void setTickerPrice(double tickerPrice) {
        this.tickerPrice = tickerPrice;
        logger.debug("Ticker price change notification( "+symbol+": "+tickerPrice + ")");
    }

    public StockType getStockType() {
        return stockType;
    }

    public void setStockType(StockType stockType) {
        this.stockType = stockType;
    }

    @Override
    public String toString() {
        return "Stock{" +
                "symbol='" + symbol + '\'' +
                ", lastDividend=" + lastDividend +
                ", fixedDividend=" + fixedDividend +
                ", parValue=" + parValue +
                ", tickerPrice=" + tickerPrice +
                '}';
    }

}
