package crawler.worker;

import logger.GuiLogger;
import logger.Logger;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Worker implements Callable<Object> {

    private final Logger logger = Logger.getLogger(GuiLogger.class);
    private ConcurrentMap<String, List<String>> graph;
    private BlockingQueue<String> queue;
    private static final int TIMEOUT = 3_000;

    public Worker(ConcurrentMap<String, List<String>> graph, BlockingQueue<String> queue) {
        this.graph = graph;
        this.queue = queue;
    }

    @Override
    public Object call() throws InterruptedException, IOException {
        String url;
        while ((url = queue.poll(TIMEOUT, TimeUnit.MILLISECONDS)) != null) {
            URL u = new URL(url);
            String asset = downloadAsset(u);
            parseAsset(u, asset);
            saveAsset(asset);
        }
        logger.info("Worker ended peacefully\n");
        return null;
    }

    private String downloadAsset(URL url) throws IOException {
        StringBuilder content = new StringBuilder();

        try {
            URLConnection urlConnection = url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line).append("\n");
            }
            bufferedReader.close();
        } catch (IOException e) {
            logger.error(String.format("Error when downloading an asset: %s\n", e.getMessage()));
            throw e;
        }

        return content.toString();
    }

    private void parseAsset(URL url, String asset) {
        List<String> foundUrls = new ArrayList<>();
        Pattern pattern = Pattern.compile("href=\"(.*?)\"", Pattern.CASE_INSENSITIVE);
        Matcher urlMatcher = pattern.matcher(asset);

        while (urlMatcher.find()) {
            String foundUrl = asset.substring(urlMatcher.start(1), urlMatcher.end(1));
            foundUrl = fixUrl(url, foundUrl);

            if (foundUrl != null && foundUrl.endsWith(".html")) {
                logger.debug(String.format("Found valid url: %s\n", foundUrl));
                foundUrls.add(foundUrl);
                if (graph.putIfAbsent(foundUrl, Collections.emptyList()) == null) queue.offer(foundUrl);
            }
        }

        graph.replace(url.toString(), foundUrls);
    }

    private void saveAsset(String asset) {
        throw new NotImplementedException();
    }

    private String fixUrl(URL url, String foundUrl) {
        if ("#".equals(foundUrl)) return null; // skip #
        if (foundUrl.startsWith("javascript")) return null; // skip javascript:void(0)
        if (foundUrl.startsWith("http")) return null; // skip external URLs
        if (foundUrl.startsWith("//")) return null; // skip external URLs without protocol

        // relative to the root URL
        if (foundUrl.startsWith("/")) return "http://" + url.getHost() + foundUrl;
        // relative to the current URL
        return url.toString().substring(0, url.toString().lastIndexOf('/') + 1) + foundUrl;
    }
}
