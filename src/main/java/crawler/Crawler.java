package crawler;

import crawler.graph.BasicAnalysis;
import crawler.graph.StronglyConnectedComponentsAnalysis;
import crawler.threadpool.ThreadPool;
import crawler.worker.AsyncWorker;
import crawler.worker.SyncWorker;
import crawler.worker.Worker;
import enums.CrawlerMode;
import logger.GuiLogger;
import logger.Logger;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public final class Crawler implements Runnable {

    private static final double nanoToMili = 1.0 / 1_000_000;
    private final Logger logger = Logger.getLogger(GuiLogger.class);

    private final URI initialUrl;
    private final CrawlerMode mode;
    private final Map<URI, List<URI>> graph;
    private final Supplier<ThreadPool> threadPoolSupplier;

    public Crawler(String url, CrawlerMode mode, Supplier<ThreadPool> supplier) throws URISyntaxException {
        initialUrl = new URI(url);
        this.mode = mode;
        graph = new ConcurrentHashMap<>();
        graph.put(this.initialUrl, Collections.emptyList());
        threadPoolSupplier = supplier;
    }

    @Override
    public void run() {
        long startingTime = System.nanoTime();
        runWorkers();
        logger.result(String.format("Workers finished after: %.4f ms%n", (System.nanoTime() - startingTime) * nanoToMili));
        analyzeGraph();
        logger.result(String.format("Graph analyzed after: %.4f ms%n", (System.nanoTime() - startingTime) * nanoToMili));
    }

    private void runWorkers() {
        ThreadPool threadPool = null;
        try {
            threadPool = threadPoolSupplier.get();
            threadPool.invokeAllAndAwait(createInitialWorker(threadPool));
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        } finally {
            if (threadPool != null) threadPool.shutdown();
        }
    }

    private Collection<Worker> createInitialWorker(ThreadPool threadPool) {
        switch (mode) {
            case SYNC:
                return Collections.singletonList(new SyncWorker(initialUrl, graph, threadPool));
            case ASYNC:
                return Collections.singletonList(new AsyncWorker(initialUrl, graph, threadPool));
            default:
                throw new IllegalStateException();
        }
    }

    private void analyzeGraph() {
        ThreadPool threadPool = null;
        try {
            threadPool = threadPoolSupplier.get();
            threadPool.submit(new BasicAnalysis(graph));
            threadPool.submit(new StronglyConnectedComponentsAnalysis(graph));
        } finally {
            if (threadPool != null) threadPool.shutdown();
        }
    }
}
