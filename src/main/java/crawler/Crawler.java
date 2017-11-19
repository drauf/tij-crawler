package crawler;

import crawler.graph.BasicAnalysis;
import crawler.worker.Worker;
import logger.GuiLogger;
import logger.Logger;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.function.Supplier;

public final class Crawler implements Runnable {

    private static final double nanoToMili = 1.0 / 1_000_000;
    private final Logger logger = Logger.getLogger(GuiLogger.class);

    private final URI initialUrl;
    private final Map<URI, List<URI>> graph;
    private final Supplier<ExecutorService> executorServiceSupplier;

    public Crawler(String url, Supplier<ExecutorService> supplier) throws URISyntaxException {
        initialUrl = new URI(url);
        graph = new ConcurrentHashMap<>();
        graph.put(this.initialUrl, Collections.emptyList());
        executorServiceSupplier = supplier;
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
        ExecutorService service = null;
        try {
            service = executorServiceSupplier.get();
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
            service = executorServiceSupplier.get();
            service.submit(new BasicAnalysis(graph));
        } finally {
            if (service != null) service.shutdown();
        }
    }
}
