package stocks.model;

/**
 * Created by allemaos on 15/01/16.
 */
public class Trade {

    private long timestamp;
    private Stock stock;
    private TradeType tradeType;
    private int shares;
    private double price;

    public Trade(){}
    public Trade(long timestamp){
        this.timestamp = timestamp;
    }
    public Trade loadTrade(Stock stock,
                 TradeType tradeType,
                 int shares,
                 double price){
        this.stock = stock;
        this.tradeType = tradeType;
        this.shares = shares;
        this.price = price;
    return this;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(   long timestamp) {
        this.timestamp = timestamp;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public TradeType getTradeType() {
        return tradeType;
    }

    public void setTradeType(TradeType tradeType) {
        this.tradeType = tradeType;
    }

    public int getShares() {
        return shares;
    }

    public void setShares(int shares) {
        this.shares = shares;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Trade{" +
                "timestamp=" + timestamp +
                ", stock=" + stock +
                ", tradeType=" + tradeType +
                ", shares=" + shares +
                ", price=" + price +
                '}';
    }
}
