package crawler.worker;

import logger.GuiLogger;
import logger.Logger;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

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
                String asset = downloadAsset(url);
                logger.debug(asset);
                parseAsset(asset);
                saveAsset(asset);
            } catch (Exception e) {
                logger.warn(String.format("Worker ending with error: %s\n", e.getMessage()));
                Exception interruptedException = new InterruptedException();
                interruptedException.addSuppressed(e);
                throw interruptedException;
            }
        }
        logger.info("Worker ending\n");
        return null;
    }

    private String downloadAsset(String theUrl) {
        StringBuilder content = new StringBuilder();

        try {
            URL url = new URL(theUrl);
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

    private void parseAsset(String asset) {
        throw new NotImplementedException();
    }

    private void saveAsset(String asset) {
        throw new NotImplementedException();
    }
}
