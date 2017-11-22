package crawler.worker;

import crawler.threadpool.ThreadPool;
import crawler.utils.CrawlerUtils;
import logger.GuiLogger;
import logger.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

public abstract class Worker implements Callable<Void> {

    private static final int requestsLimit = 3000;
    static final String saveToPath = "/Projects/tij/";
    static final Logger logger = Logger.getLogger(GuiLogger.class);

    final URI urlToParse;
    final Map<URI, List<URI>> graph;
    final ThreadPool threadPool;

    Worker(URI url, Map<URI, List<URI>> graph, ThreadPool threadPool) {
        this.urlToParse = url;
        this.graph = graph;
        this.threadPool = threadPool;
    }

    @Override
    public Void call() throws InterruptedException {
        if (graph.size() > requestsLimit) {
            graph.putIfAbsent(urlToParse, Collections.emptyList());
            return null;
        }

        try {
            Document document = downloadDocument(urlToParse);
            saveDocument(urlToParse, document);
            parseDocument(urlToParse, document);
        } catch (IOException ignored) {
        }
        logger.debug("Worker ended peacefully\n");
        return null;
    }

    protected abstract Document downloadDocument(URI url) throws IOException;

    protected abstract void saveDocument(URI url, Document document) throws IOException;

    protected abstract Worker createWorker(URI uri);

    private void parseDocument(URI baseUrl, Document document) throws InterruptedException {
        List<URI> allFoundUrls = new ArrayList<>();
        List<URI> notVisitedUrls = new ArrayList<>();

        for (Element link : document.select("a")) {
            String foundUrl = link.attr("href");

            CrawlerUtils.validateUrl(baseUrl, foundUrl).ifPresent(url -> {
                allFoundUrls.add(url);
                if (graph.putIfAbsent(url, Collections.emptyList()) == null) {
                    notVisitedUrls.add(url);
                }
            });
        }

        graph.replace(baseUrl, allFoundUrls);
        executeRecursiveTasks(notVisitedUrls);
    }

    private void executeRecursiveTasks(List<URI> notVisitedUrls) throws InterruptedException {
        List<Worker> workers = notVisitedUrls.stream()
                .map(this::createWorker)
                .collect(Collectors.toList());
        threadPool.invokeAll(workers);
    }
}
