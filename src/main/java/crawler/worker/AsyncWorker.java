package crawler.worker;

import crawler.threadpool.ThreadPool;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Map;

public class AsyncWorker extends Worker {
    public AsyncWorker(URI url, Map<URI, List<URI>> graph, ThreadPool threadPool) {
        super(url, graph, threadPool);
    }

    @Override
    protected Document downloadDocument(URI url) throws IOException {
        return Jsoup.connect(url.toString()).get();
    }

    @Override
    protected void saveDocument(URI url, Document document) throws IOException {
        String targetPath = saveToPath + url.toString().replace('/', '_');
        Path file = Paths.get(targetPath);
        try (AsynchronousFileChannel asyncFile = AsynchronousFileChannel.open(file,
                StandardOpenOption.WRITE,
                StandardOpenOption.CREATE)) {

            asyncFile.write(ByteBuffer.wrap(document.toString().getBytes()), 0);
        } catch (IOException e) {
            logger.error(String.format("Error when saving an asset: %s%n", e.getMessage()));
            throw e;
        }
    }

    @Override
    protected Worker createWorker(URI uri) {
        return new AsyncWorker(uri, graph, threadPool);
    }
}
