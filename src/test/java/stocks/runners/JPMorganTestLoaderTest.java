package stocks.runners;

import org.junit.Assert;
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
import stocks.utils.Utils;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by allemaos on 18/01/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class JPMorganTestLoaderTest {
    private static final Logger log = LoggerFactory.getLogger(JPMorganTestLoaderTest.class);

    @Autowired
    private StockRepository stockRepository;
    @Autowired
    private StockManager stockManager;
    @Autowired
    private StockService stockService;


    @Test
    public void testRun() throws Exception {

        new StockRepoStocksLoader();
        new StockRepoTradesLoader();

        log.info("Loading..");
        boolean out = stockManager.load();
        ArrayList<Trade> test_trades = stockManager.getTrades();
        for(Trade test_t:test_trades)
            log.info(test_t.toString());
        log.info("StockManager is Loaded: " + out);

        for(Trade t: stockManager.getTrades()){
            boolean result = stockService.recordTrade(t);
        }


        for(String stockSymbol: stockRepository.getAllStockSymbols()){
            log.info(stockSymbol+" - updated stock after recordTrade: "+stockManager.getStocks().get(stockSymbol));
        }

        log.info("# Stocks Table:");
        for(String stockSymbol: stockRepository.getAllStockSymbols()){
            log.info(stockSymbol+" -->"+stockManager.getStocks().get(stockSymbol));
        }

        log.info("# Dividend Yield for every stock:");
        for(String symbol: stockRepository.getAllStockSymbols()){
            if(stockRepository.getBySymbol(symbol).getLastDividend()!=0.0) {
                double dividendYield = stockService.calculateDividendYield(symbol);
                log.info(symbol + " - : " + Utils.roundDecimal(dividendYield));
                Assert.assertTrue(dividendYield >= 0.0);
            }
        }

        log.info("# PERatio for every stock:");
        for(String symbol: stockRepository.getAllStockSymbols()){
            if(stockRepository.getBySymbol(symbol).getLastDividend()!=0.0) {
                double calculatePERatio = stockService.calculatePERatio(symbol);
                log.info(symbol + " - : " + Utils.roundDecimal(calculatePERatio));
                Assert.assertTrue(calculatePERatio >= 0.0);
            }
        }

        log.info("# Stock Price for every stock:");
        for(String symbol: stockRepository.getAllStockSymbols()){
            if(stockRepository.getBySymbol(symbol).getLastDividend()!=0.0) {
                double calculateStockPrice = stockService.calculateStockPrice(symbol);
                log.info(symbol + " - : " + Utils.roundDecimal(calculateStockPrice));
                Assert.assertTrue(calculateStockPrice >= 0.0);
            }
        }

        double calculateGBCEAllShareIndex = stockService.calculateGBCEAllShareIndex();
        log.info("# GBCEAllShareIndex: "+Utils.roundDecimal(calculateGBCEAllShareIndex));
        Assert.assertTrue(calculateGBCEAllShareIndex >= 0.0);
        log.info("NOTE: LastDividend of TEA is 0.0 - this stock is not valid for our calculations");

    }
}