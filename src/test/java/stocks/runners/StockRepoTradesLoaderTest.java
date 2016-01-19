package stocks.runners;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import stocks.Application;
import stocks.StockManager;
import stocks.StockRepository;
import stocks.StockService;
import stocks.model.Trade;
import stocks.model.TradeType;
import stocks.utils.Utils;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.Assert.*;

/**
 * Created by allemaos on 18/01/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class StockRepoTradesLoaderTest {

    private static final Logger log = LoggerFactory.getLogger(StockRepoTradesLoaderTest.class);

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private StockManager stockManager;

    @Autowired
    private StockService stockService;

    @Test
    public void testRun() throws Exception {

        new StockRepoStocksLoader();

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
        int shares;
        log.info(".... Initializing trades");
        ArrayList<Trade> trades = new ArrayList<Trade>();
        for (int i=0;i<5;i++){
            shares = 1+rand.nextInt(99);
            price = stockInitPrices.get(i);
            TradeType type = (rand.nextInt(100) > 50) ? TradeType.SELL : TradeType.BUY;
            t = System.currentTimeMillis();

            log.info("Generated : (" + stockNames.get(i) +") " + shares + "["+type +"], (price: " + price + ")");
            Trade temp_t = stockRepository.getByTimestamp(t);
            temp_t.loadTrade(stockRepository.getBySymbol(stockNames.get(i)), type, shares, price);
            trades.add(temp_t);
            log.debug(temp_t.getTimestamp() + " -->" + temp_t);

            boolean out = stockManager.load();
            log.debug("StockManager is updated: " + out);
            out = stockService.recordTrade(temp_t);
            log.debug("Trade is recorded:" + out);

        }

        for (int i=0;i<5;i++) {
            log.info(trades.get(i).getTimestamp() + " -->" + trades.get(i));
        }

        log.info(".... Loading more trades");
        int randi;
        double curr_price;
        int num_trades = 20;
        for (int i=0;i<num_trades-5;i++){
            randi = rand.nextInt(4);
            shares = 1+rand.nextInt(99);
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
            trades.add(temp_t);
            log.debug(temp_t.getTimestamp() + " -->" + temp_t);

            log.debug("Loading StockManager:");
            boolean out = stockManager.load();
            log.debug("StockManager is updated: " + out);
            out = stockService.recordTrade(temp_t);
            log.debug("Trade is recorded:" + out);

/*
            log.info(t + " -->" + stockRepository.getByTimestamp(t)
                    .loadTrade(stockRepository.getBySymbol(stockNames.get(randi)),
                            type, shares, stockInitPrices.get(randi)+ price));
*/
        }

        log.info("All Trades size: "+ stockRepository.getAllTradeTimestamps().size());

        for (int i=0;i<num_trades;i++) {
            log.info(trades.get(i).getTimestamp() + " -->" + trades.get(i));
        }


    }
}
