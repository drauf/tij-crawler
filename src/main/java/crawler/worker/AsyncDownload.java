package crawler.worker;

import crawler.threadpool.ThreadPool;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.Response;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import static org.asynchttpclient.Dsl.*;

public class AsyncDownload implements Callable<Void> {

    private static AtomicInteger documentsParsed = new AtomicInteger(0);
    private static AsyncHttpClient asyncHttpClient = asyncHttpClient();
    private static final int requestsLimit = 3000;

    final URI urlToDownload;
    final Map<URI, List<URI>> graph;
    final ThreadPool threadPool;

    public AsyncDownload(URI url, Map<URI, List<URI>> graph, ThreadPool threadPool) {
        this.urlToDownload = url;
        this.graph = graph;
        this.threadPool = threadPool;
    }

    @Override
    public Void call() throws Exception {
        if (documentsParsed.get() > requestsLimit) {
            graph.putIfAbsent(urlToDownload, Collections.emptyList());
            return null;
        }

        documentsParsed.incrementAndGet();
        Future<Response> whenResponse = asyncHttpClient.prepareGet(urlToDownload.toString()).execute();
        Parse task = new Parse(urlToDownload, graph, threadPool, whenResponse);
        threadPool.invokeAll(Collections.singletonList(task));

        return null;
    }
}
