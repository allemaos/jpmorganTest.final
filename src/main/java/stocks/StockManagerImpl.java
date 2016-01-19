package stocks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import stocks.model.Stock;
import stocks.model.Trade;

import java.util.HashMap;
import java.util.ArrayList;

/**
 * Created by allemaos on 15/01/16.
 */
@Service
public class StockManagerImpl implements StockManager {

    private static final Logger log = LoggerFactory.getLogger(StockManagerImpl.class);

    private HashMap<String, Stock> stocks = new HashMap<String, Stock>();
    private ArrayList<Trade> trades = new ArrayList<Trade>();

    @Autowired
    private StockRepository stockRepository;

    public StockManagerImpl(){
    }

    public boolean load(){
        boolean out = false;
        log.debug(" StockManager is starting loadind to Stock Repository ");
        out = loadTrades();
        out = out && loadStocks();
        log.debug("Stock Repository is loaded to StockManager");
        if(out == false ) log.error("Stock Repository wasn't loaded to StockManager");
        return out;
    }

    private boolean loadStocks(){
        for (String s:stockRepository.getAllStockSymbols()){
            stocks.put(s,stockRepository.getBySymbol(s));
        }
        return true;
    }
    private boolean loadTrades(){
        ArrayList<Long> arrayt = stockRepository.getAllTradeTimestamps();
        for (Long t:arrayt){
            log.debug(stockRepository.getByTimestamp(t).toString());
        }
        for (Long t:arrayt){
            Trade temp_t = stockRepository.getByTimestamp(t);
            trades.add(temp_t);
        }
        return true;
    }

    public HashMap<String, Stock> getStocks() {
        return stocks;
    }

    public void setStocks(HashMap<String, Stock> stocks) {
        this.stocks = stocks;
    }

    @Override
    public boolean recordTrade(Trade trade) throws Exception {
        boolean out = false;
        out = trades.contains(trade);
        return out;
    }

    public ArrayList<Trade> getTrades() {
        return trades;
    }

    @Override
    public Stock getStockBySymbol(String symbol) {
        return stocks.get(symbol);
    }

    public void setTrades(ArrayList<Trade> trades) {
        this.trades = trades;
    }
}
