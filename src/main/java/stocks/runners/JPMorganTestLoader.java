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
import stocks.utils.Utils;

import java.util.ArrayList;

/**
 * Created by allemaos on 18/01/16.
 */
@Order(3)
@Component
public class JPMorganTestLoader  implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(JPMorganTestLoader.class);
    @Autowired
    private StockRepository stockRepository;
    @Autowired
    private StockManager stockManager;
    @Autowired
    private StockService stockService;

    @Override
    public void run(String... args) throws Exception {

        log.debug("Loading StockManager:");
        boolean out = stockManager.load();
        log.debug("StockManager is Loaded: " + out);

        boolean result , prod_result=true;
        for(Trade t: stockManager.getTrades()){
            result = stockService.recordTrade(t);
            prod_result = prod_result && result;
        }
        if (prod_result==true)
            log.debug("All Trades are recorded in Manager");



        log.info("# Stocks Table:");
        for(String stockSymbol: stockRepository.getAllStockSymbols()){
            log.info(stockSymbol+" -->"+stockManager.getStocks().get(stockSymbol));
        }

        log.info("# Dividend Yield for every stock:");
        for(String symbol: stockRepository.getAllStockSymbols()){
            if(stockRepository.getBySymbol(symbol).getLastDividend()!=0.0) {
                double dividendYield = stockService.calculateDividendYield(symbol);
                log.info(symbol + " - : " + Utils.roundDecimal(dividendYield));
            }
        }

        log.info("# PERatio for every stock:");
        for(String symbol: stockRepository.getAllStockSymbols()){
            if(stockRepository.getBySymbol(symbol).getLastDividend()!=0.0) {
                double calculatePERatio = stockService.calculatePERatio(symbol);
                log.info(symbol + " - : " + Utils.roundDecimal(calculatePERatio));
            }
        }

        log.info("# Stock Price for every stock:");
        for(String symbol: stockRepository.getAllStockSymbols()){
            if(stockRepository.getBySymbol(symbol).getLastDividend()!=0.0) {
                double calculateStockPrice = stockService.calculateStockPrice(symbol);
                log.info(symbol + " - : " + Utils.roundDecimal(calculateStockPrice));
            }
        }

        log.info("# GBCEAllShareIndex: "+Utils.roundDecimal(stockService.calculateGBCEAllShareIndex()));
        log.info("NOTE: LastDividend of TEA is 0.0 - this stock is not valid for our calculations");

    }

}
