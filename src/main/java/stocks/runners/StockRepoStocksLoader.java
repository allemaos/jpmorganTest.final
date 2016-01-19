package stocks.runners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import stocks.StockRepository;
import stocks.model.StockType;

/**
 * Created by allemaos on 17/01/16.
 */

@Order(1)
@Component
public class StockRepoStocksLoader implements CommandLineRunner{

    private static final Logger log = LoggerFactory.getLogger(StockRepoStocksLoader.class);

    @Autowired
    private StockRepository stockRepository;

    @Override
    public void run(String... args) throws Exception {

        log.info(".... Loading stocks");
        log.info("TEA -->" + stockRepository.getBySymbol("TEA").loadStock(0.0, 0.0, 100.0, StockType.COMMON));
        log.info("POP -->" + stockRepository.getBySymbol("POP").loadStock(8.0, 0.0, 100.0, StockType.COMMON));
        log.info("ALE -->" + stockRepository.getBySymbol("ALE").loadStock(23.0, 0.0, 60.0, StockType.COMMON));
        log.info("GIN -->" + stockRepository.getBySymbol("GIN").loadStock(8.0, 0.02, 100.0, StockType.PREFERRED));
        log.info("JOE -->" + stockRepository.getBySymbol("JOE").loadStock(13.0, 0.0, 250.0, StockType.COMMON));

    }
}
