package crawler.worker;

import crawler.threadpool.ThreadPool;
import crawler.utils.CrawlerUtils;
import logger.GuiLogger;
import logger.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

public class Worker implements Callable<Void> {

    private static final String saveToPath = "/Projects/tij/";
    private static final Logger logger = Logger.getLogger(GuiLogger.class);

    private final URI urlToParse;
    private final Map<URI, List<URI>> graph;
    private final ThreadPool threadPool;

    public Worker(URI url, Map<URI, List<URI>> graph, ThreadPool threadPool) {
        this.urlToParse = url;
        this.graph = graph;
        this.threadPool = threadPool;
    }

    @Override
    public Void call() throws InterruptedException {
        try {
            Document document = downloadDocument(urlToParse);
            parseDocument(urlToParse, document);
            saveDocument(urlToParse, document);
        } catch (IOException ignored) {
        }
        logger.debug("Worker ended peacefully\n");
        return null;
    }

    private Document downloadDocument(URI url) throws IOException {
        return Jsoup.connect(url.toString()).get();
    }

    private void parseDocument(URI baseUrl, Document document) throws InterruptedException {
        List<URI> allFoundUrls = new ArrayList<>();
        List<URI> notVisitedUrls = new ArrayList<>();

        for (Element link : document.select("a")) {
            String foundUrl = link.attr("href");

            CrawlerUtils.validateUrl(baseUrl, foundUrl).ifPresent(url -> {
                if (!CrawlerUtils.isDocument(url)) return;

                logger.debug(String.format("Found valid url: %s\n", url));
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
                .map(url -> new Worker(url, graph, threadPool))
                .collect(Collectors.toList());
        threadPool.invokeAll(workers);
    }

    private void saveDocument(URI url, Document document) throws IOException {
        try {
            String targetPath = saveToPath + url.toString().replace('/', '_');
            Files.write(Paths.get(targetPath), document.toString().getBytes(Charset.forName("UTF-8")));
        } catch (IOException e) {
            logger.error(String.format("Error when saving an asset: %s%n", e.getMessage()));
            throw e;
        }
    }
}
