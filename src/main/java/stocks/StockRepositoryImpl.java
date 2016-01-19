package stocks;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import stocks.model.Stock;
import stocks.model.Trade;

import java.util.ArrayList;

/**
 * Created by allemaos on 16/01/16.
 */
@Component
public class StockRepositoryImpl implements StockRepository{

    ArrayList<String> stock_refs = new ArrayList<String>();
    ArrayList<Long> trade_refs = new ArrayList<Long>();

    @Override
    @Cacheable("stocks")
    public Stock getBySymbol(String symbol){
        if(!stock_refs.contains(symbol)) stock_refs.add(symbol);
        return new Stock(symbol);
    }

    @Override
    @Cacheable("trades")
    public Trade getByTimestamp(long timestamp){
        if(!trade_refs.contains(timestamp)) {
            trade_refs.add(timestamp);
            slowdown();
        }
        return new Trade(timestamp);
    }

    public ArrayList<String> getAllStockSymbols(){
        return stock_refs;
    }

    public ArrayList<Long> getAllTradeTimestamps(){
        ArrayList<Trade> trades = new ArrayList<Trade>();
        return trade_refs;
    }
    //todo: need to remove it and fix indexing on trades
    private void slowdown() {
        try {
            long time = 1;
            Thread.currentThread().sleep(time);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }

}
