package crawler.worker;

import crawler.threadpool.ThreadPool;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;

public class AsyncWorker extends Worker {
    public AsyncWorker(URI url, Map<URI, List<URI>> graph, ThreadPool threadPool) {
        super(url, graph, threadPool);
    }

    @Override
    protected Document downloadDocument(URI url) throws IOException {
        return null;
    }

    @Override
    protected void saveDocument(URI url, Document document) throws IOException {

    }

    @Override
    protected Worker createWorker(URI uri) {
        return new AsyncWorker(uri, graph, threadPool);
    }
}
