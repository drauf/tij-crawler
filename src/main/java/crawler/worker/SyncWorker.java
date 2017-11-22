package crawler.worker;

import crawler.threadpool.ThreadPool;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public class SyncWorker extends Worker {
    public SyncWorker(URI url, Map<URI, List<URI>> graph, ThreadPool threadPool) {
        super(url, graph, threadPool);
    }

    @Override
    protected Document downloadDocument(URI url) throws IOException {
        return Jsoup.connect(url.toString()).get();
    }

    @Override
    protected void saveDocument(URI url, Document document) throws IOException {
        try {
            String targetPath = saveToPath + url.toString().replace('/', '_');
            Files.write(Paths.get(targetPath), document.toString().getBytes(Charset.forName("UTF-8")));
        } catch (IOException e) {
            logger.error(String.format("Error when saving an asset: %s%n", e.getMessage()));
            throw e;
        }
    }

    @Override
    protected Worker createWorker(URI uri) {
        return new SyncWorker(uri, graph, threadPool);
    }
}
