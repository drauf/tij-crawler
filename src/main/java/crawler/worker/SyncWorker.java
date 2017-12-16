package crawler.worker;

import crawler.threadpool.ThreadPool;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.activation.UnsupportedDataTypeException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
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
    protected Document downloadDocument(URI uri) throws IOException {
        URL url = new URL(uri.toString());
        URLConnection conn = url.openConnection();
        String contentType = conn.getContentType();
        if (contentType == null || !contentType.startsWith("text/")) throw new UnsupportedDataTypeException();

        StringBuilder sb = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            String inputLine;
            while ((inputLine = in.readLine()) != null)
                sb.append(inputLine);
        }

        return Jsoup.parse(sb.toString());
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
