package crawler;

import crawler.graph.BasicAnalysis;
import crawler.worker.Worker;
import logger.GuiLogger;
import logger.Logger;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.IntStream;

public class Crawler implements Runnable {

    private final Logger logger = Logger.getLogger(GuiLogger.class);
    private final int NUMBER_OF_THREADS;
    private final ConcurrentMap<URL, List<URL>> graph;
    private final BlockingQueue<URL> queue;

    public Crawler(String initialUrl, int threads) throws MalformedURLException {
        NUMBER_OF_THREADS = threads;
        URL url = new URL(initialUrl);
        graph = new ConcurrentHashMap<>();
        graph.put(url, Collections.emptyList());
        queue = new LinkedBlockingQueue<>();
        queue.offer(url);
    }

    @Override
    public void run() {
        runWorkers();
        analyzeGraph();
    }

    private void runWorkers() {
        ExecutorService service = null;
        try {
            service = Executors.newCachedThreadPool();
            Collection<? extends Callable<Object>> workers = createWorkers(graph, queue, NUMBER_OF_THREADS);
            logger.debug("Invoking worker threads\n");
            service.invokeAll(workers);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        } finally {
            if (service != null) service.shutdown();
        }
        logger.info("All workers finished\n");
    }

    private List<? extends Callable<Object>> createWorkers(ConcurrentMap<URL, List<URL>> graph, BlockingQueue<URL> queue, int count) {
        final List<Worker> workers = new ArrayList<>();
        IntStream.rangeClosed(1, count)
                .forEach(i -> workers.add(new Worker(graph, queue)));
        return workers;
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
