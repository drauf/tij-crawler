package crawler.worker;

import logger.GuiLogger;
import logger.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Worker implements Callable<Object> {

    private static final int TIMEOUT = 1_000;
    private static final String saveToPath = "/Projects/tij/";
    private static final Logger logger = Logger.getLogger(GuiLogger.class);

    private ConcurrentMap<URL, List<URL>> graph;
    private BlockingQueue<URL> queue;

    public Worker(ConcurrentMap<URL, List<URL>> graph, BlockingQueue<URL> queue) {
        this.graph = graph;
        this.queue = queue;
    }

    @Override
    public Object call() throws InterruptedException, IOException {
        URL url;
        while ((url = queue.poll(TIMEOUT, TimeUnit.MILLISECONDS)) != null) {
            String asset = downloadAsset(url);
            parseAsset(url, asset);
            saveAsset(url, asset);
        }
        logger.debug("Worker ended peacefully\n");
        return null;
    }

    private String downloadAsset(URL url) throws IOException {
        StringBuilder content = new StringBuilder();

        URLConnection urlConnection = url.openConnection();
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            logger.error(String.format("Error when downloading an asset: %s\n", e.getMessage()));
            throw e;
        }

        return content.toString();
    }

    private void parseAsset(URL url, String asset) {
        List<URL> foundUrls = new ArrayList<>();
        Pattern pattern = Pattern.compile("href=\"(.*?)\"", Pattern.CASE_INSENSITIVE);
        Matcher urlMatcher = pattern.matcher(asset);

        while (urlMatcher.find()) {
            Optional<URL> foundUrl = validateUrl(url, asset.substring(urlMatcher.start(1), urlMatcher.end(1)));

            foundUrl.ifPresent(u -> {
                logger.debug(String.format("Found valid url: %s\n", foundUrl));
                foundUrls.add(u);
                if (graph.putIfAbsent(u, Collections.emptyList()) == null) queue.offer(u);
            });
        }

        graph.replace(url, foundUrls);
    }

    private void saveAsset(URL url, String asset) throws IOException {
        try {
            String targetPath = saveToPath + url.getPath().replace('/', '_');
            Files.write(Paths.get(targetPath), asset.getBytes());
        } catch (IOException e) {
            logger.error(String.format("Error when saving an asset: %s\n", e.getMessage()));
            throw e;
        }
    }

    private Optional<URL> validateUrl(URL url, String foundUrl) {
        if (!foundUrl.startsWith("http")) {
            foundUrl = "http://" + url.getHost() + url.getPath().substring(0, url.getPath().lastIndexOf('/') + 1) + foundUrl;
        }

        try {
            URL u = new URL(foundUrl);
            return !u.getPath().equals("/") && u.getHost().equals(url.getHost()) ? Optional.of(u) : Optional.empty();
        } catch (MalformedURLException e) {
            return Optional.empty();
        }
    }
}
