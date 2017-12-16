package crawler.worker;

import crawler.threadpool.ThreadPool;
import crawler.utils.CrawlerUtils;
import org.asynchttpclient.Response;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class Parse implements Callable<Void> {

    final URI urlToParse;
    final Map<URI, List<URI>> graph;
    final ThreadPool threadPool;
    final Future<Response> whenResponse;

    Parse(URI url, Map<URI, List<URI>> graph, ThreadPool threadPool, Future<Response> whenResponse) {
        this.urlToParse = url;
        this.graph = graph;
        this.threadPool = threadPool;
        this.whenResponse = whenResponse;
    }

    @Override
    public Void call() throws Exception {
        if (!whenResponse.isDone()) {
            try {
                threadPool.invokeAll(Collections.singletonList(this));
                return null;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        List<URI> allFoundUrls = new ArrayList<>();
        List<URI> notVisitedUrls = new ArrayList<>();

        Document document = new Document(whenResponse.get().getResponseBody());
        for (Element link : document.select("a")) {
            String foundUrl = link.attr("href");

            CrawlerUtils.validateUrl(urlToParse, foundUrl).ifPresent(url -> {
                allFoundUrls.add(url);
                if (graph.putIfAbsent(url, Collections.emptyList()) == null) {
                    notVisitedUrls.add(url);
                }
            });
        }
        graph.replace(urlToParse, allFoundUrls);

        List<Callable<Void>> workers = notVisitedUrls.stream()
                .map(url -> new AsyncWorker(url, graph, threadPool))
                .collect(Collectors.toList());
        threadPool.invokeAll(workers);
        threadPool.invokeAll(Collections.singletonList(new AsyncSave(urlToParse, document)));

        return null;
    }
}
