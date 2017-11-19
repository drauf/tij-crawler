package crawler;

import java.net.URISyntaxException;
import java.util.concurrent.Executors;

public class FixedThreadPoolCrawler extends Crawler {
    public FixedThreadPoolCrawler(String url, int threads) throws URISyntaxException {
        super(url);
        executorServiceSupplier = () -> Executors.newFixedThreadPool(threads);
    }
}
