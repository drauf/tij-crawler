package crawler.worker;

import crawler.threadpool.ThreadPool;
import crawler.utils.CrawlerUtils;
import logger.GuiLogger;
import logger.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
            String asset = downloadAsset(urlToParse);
            parseAsset(urlToParse, asset);
            saveAsset(urlToParse, asset);
        } catch (IOException ignored) {
        }
        logger.debug("Worker ended peacefully\n");
        return null;
    }

    private String downloadAsset(URI url) throws IOException {
        StringBuilder content = new StringBuilder();

        URLConnection urlConnection = new URL(url.toString()).openConnection();
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), Charset.forName("UTF-8")))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            logger.error(String.format("Error when downloading an asset: %s%n", e.getMessage()));
            throw e;
        }

        return content.toString();
    }

    private void parseAsset(URI baseUrl, String asset) throws InterruptedException {
        List<URI> allFoundUrls = new ArrayList<>();
        List<URI> notVisitedUrls = new ArrayList<>();
        Pattern pattern = Pattern.compile("href=\"(.*?)\"", Pattern.CASE_INSENSITIVE);
        Matcher urlMatcher = pattern.matcher(asset);

        while (urlMatcher.find()) {
            String foundUrl = asset.substring(urlMatcher.start(1), urlMatcher.end(1));
            Optional<URI> validatedUrl = CrawlerUtils.validateUrl(baseUrl, foundUrl);

            validatedUrl.ifPresent(url -> {
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

    private void saveAsset(URI url, String asset) throws IOException {
        try {
            String targetPath = saveToPath + url.getPath().replace('/', '_');
            Files.write(Paths.get(targetPath), asset.getBytes(Charset.forName("UTF-8")));
        } catch (IOException e) {
            logger.error(String.format("Error when saving an asset: %s%n", e.getMessage()));
            throw e;
        }
    }
}
