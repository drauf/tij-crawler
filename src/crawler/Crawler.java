package crawler;

import crawler.graph.BasicAnalysis;
import crawler.worker.Worker;
import logger.GuiLogger;
import logger.Logger;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Crawler implements Runnable {

    private static final double nanoToMili = 1.0 / 1_000_000;
    private final Logger logger = Logger.getLogger(GuiLogger.class);

    private final int numberOfThreads;
    private final URL initialUrl;
    private final ConcurrentMap<URL, List<URL>> graph;

    public Crawler(String url, int threads) throws MalformedURLException {
        numberOfThreads = threads;
        initialUrl = new URL(url);
        graph = new ConcurrentHashMap<>();
        graph.put(this.initialUrl, Collections.emptyList());
    }

    @Override
    public void run() {
        long startingTime = System.nanoTime();
        runWorkers();
        logger.result(String.format("Workers finished after: %.4f ms\n", (System.nanoTime() - startingTime) * nanoToMili));
        analyzeGraph();
        logger.result(String.format("Graph analyzed after: %.4f ms\n", (System.nanoTime() - startingTime) * nanoToMili));
    }

    private void runWorkers() {
        ExecutorService service = null;
        try {
            service = Executors.newFixedThreadPool(numberOfThreads);
            service.invokeAll(Collections.singletonList(new Worker(initialUrl, graph, service)));
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        } finally {
            if (service != null) service.shutdown();
        }
    }

    private void analyzeGraph() {
        ExecutorService service = null;
        try {
            service = Executors.newCachedThreadPool();
            service.submit(new BasicAnalysis(graph));
        } finally {
            if (service != null) service.shutdown();
        }
    }
}
