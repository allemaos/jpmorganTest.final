package stocks.runners;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import stocks.Application;
import stocks.StockRepository;
import stocks.model.Stock;
import stocks.model.StockType;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Created by allemaos on 18/01/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class StockRepoStocksLoaderTest {
    private static final Logger log = LoggerFactory.getLogger(StockRepoStocksLoaderTest.class);

    @Autowired
    private StockRepository stockRepository;

    @Test
    public void testRun() throws Exception {

        log.info("StockRepoStocksLoaderTesting..");
        String symbol = "TEA";
        log.info(symbol + " -->" + stockRepository.getBySymbol(symbol).loadStock(0.0, 0.0, 100.0, StockType.COMMON));

        Stock s = stockRepository.getBySymbol(symbol);
        s.setTickerPrice(5000.0);
        log.info("TEA -->" + s);

        log.info("TEA new ticker price -->" + stockRepository.getBySymbol(symbol));
        assertThat(stockRepository.getBySymbol("TEA").getTickerPrice(),equalTo(5000.0));
    }
}