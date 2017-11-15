package crawler.worker;

import logger.GuiLogger;
import logger.Logger;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.BufferedReader;
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

    public Worker(ConcurrentMap<String, List<String>> graph, BlockingQueue<String> queue) {
        this.graph = graph;
        this.queue = queue;
    }

    @Override
    public Object call() throws Exception {
        String url;
        while ((url = queue.poll(1, TimeUnit.SECONDS)) != null) {
            try {
                URL u = new URL(url);
                String asset = downloadAsset(u);
                parseAsset(u, asset);
                saveAsset(asset);
            } catch (Exception e) {
                logger.error("Worker exited with an error\n");
                e.printStackTrace();
                Exception interruptedException = new InterruptedException();
                interruptedException.addSuppressed(e);
                throw interruptedException;
            }
        }
        logger.info("Worker ended peacefully\n");
        return null;
    }

    private String downloadAsset(URL url) {
        StringBuilder content = new StringBuilder();

        try {
            URLConnection urlConnection = url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line).append("\n");
            }
            bufferedReader.close();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        return content.toString();
    }

    private void parseAsset(URL url, String asset) {
        logger.debug("Start asset parsing\n");
        List<String> foundUrls = new ArrayList<>();
        Pattern pattern = Pattern.compile("href=\"(.*?)\"", Pattern.CASE_INSENSITIVE);
        Matcher urlMatcher = pattern.matcher(asset);

        while (urlMatcher.find()) {
            String foundUrl = asset.substring(urlMatcher.start(1), urlMatcher.end(1));
            foundUrl = fixUrl(url, foundUrl);

            if (foundUrl != null) {
                logger.debug(String.format("Found url: %s\n", foundUrl));
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

        if (foundUrl.startsWith("//")) { // URL with skipped protocol
            return "http:" + foundUrl;
        }

        if (!foundUrl.startsWith("http")) { // relative URL
            if (foundUrl.startsWith("/")) { // relative to root URL
                return "http://" + url.getHost() + foundUrl;
            } else {  // relative to current URL
                return url.toString() + foundUrl.substring(1);
            }
        }
        return foundUrl; // regular URL starting with http or https
    }
}
