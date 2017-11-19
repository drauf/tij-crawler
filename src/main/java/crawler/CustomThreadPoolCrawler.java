package crawler;

import java.net.URISyntaxException;

public class CustomThreadPoolCrawler extends Crawler {
    public CustomThreadPoolCrawler(String url, int threads) throws URISyntaxException {
        super(url);
        executorServiceSupplier = () -> new CustomThreadPool(threads);
    }
}
