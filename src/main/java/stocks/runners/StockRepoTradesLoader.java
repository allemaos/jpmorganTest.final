package stocks.runners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import stocks.StockManager;
import stocks.StockRepository;
import stocks.StockService;
import stocks.model.Trade;
import stocks.model.TradeType;
import stocks.utils.Utils;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by allemaos on 17/01/16.
 */

@Order(2)
@Component
public class StockRepoTradesLoader implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(StockRepoTradesLoader.class);

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private StockManager stockManager;
    @Autowired
    private StockService stockService;

    @Override
    public void run(String... args) throws Exception {

        log.info(".... Trades Phase:");
        long t;
        double price, p;
        Random rand = new Random();
        ArrayList<String> stockNames = new ArrayList<String>();
        stockNames.add("TEA");
        stockNames.add("POP");
        stockNames.add("ALE");
        stockNames.add("GIN");
        stockNames.add("JOE");

        ArrayList<Double> stockInitPrices = new ArrayList<Double>();
        stockInitPrices.add(10.0);
        stockInitPrices.add(20.0);
        stockInitPrices.add(30.0);
        stockInitPrices.add(40.0);
        stockInitPrices.add(50.0);
        int shares, stock_index;
        Double topShares;
        log.info(".... Initializing trades");
        for (int i=0;i<5;i++){
            stock_index = i;
            topShares = new Double(stockRepository.getBySymbol(stockNames.get(stock_index)).getLastDividend());
            if(topShares == 0) {
                log.info("**There are no shares to trade for this stock");
                continue;
            }
            shares = 1 + rand.nextInt(topShares.intValue()-1);
            price = stockInitPrices.get(stock_index);
            TradeType type = (rand.nextInt(100) > 50) ? TradeType.SELL : TradeType.BUY;
            t = System.currentTimeMillis();

            log.info("Generated : (" + stockNames.get(stock_index) +") " + shares + "["+type +"], (price: " + price + ")");
            Trade temp_t = stockRepository.getByTimestamp(t);
            temp_t.loadTrade(stockRepository.getBySymbol(stockNames.get(stock_index)), type, shares, price);

            boolean out = stockManager.load();
            log.debug("StockManager is updated: " + out);
            out = stockService.recordTrade(temp_t);
            log.debug("Trade is recorded:" + out);

        }

        log.info(".... Loading more trades");
        int randi;
        double curr_price;
        int num_trades = 20;
        for (int i=0;i<num_trades;i++){
            randi = rand.nextInt(4);
            topShares = new Double(stockRepository.getBySymbol(stockNames.get(randi)).getLastDividend());
            if(topShares == 0) {
                log.info("**There are no shares to trade for this stock");
                continue;
            }
            shares = 1 + rand.nextInt(topShares.intValue()-1);
            //shares = 1+rand.nextInt(99);

            TradeType type = (rand.nextInt(100) > 50) ? TradeType.SELL : TradeType.BUY;
            t = System.currentTimeMillis();

            curr_price = stockRepository.getBySymbol(stockNames.get(randi)).getTickerPrice();
            if(curr_price != 0.0) {
                price = Utils.roundDecimal(curr_price + rand.nextDouble());
                log.info("@stock current price: " + curr_price + " and simulated price: " + price);
            }
            else
                price = Utils.roundDecimal(stockInitPrices.get(randi) + rand.nextDouble());

            log.info("Generated : (" + stockNames.get(randi) +") " + shares + "["+type +"], (price: " + price + ")");
            Trade temp_t = stockRepository.getByTimestamp(t);
            temp_t.loadTrade(stockRepository.getBySymbol(stockNames.get(randi)), type, shares, price);

            log.debug("Loading StockManager:");
            boolean out = stockManager.load();
            log.debug("StockManager is updated: " + out);
            out = stockService.recordTrade(temp_t);
            log.debug("Trade is recorded:" + out);

        }

        log.info("All Trades size: "+ stockRepository.getAllTradeTimestamps().size());



    }
}