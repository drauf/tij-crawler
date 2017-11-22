package crawler.graph;

import logger.GuiLogger;
import logger.Logger;

import java.net.URI;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class BasicAnalysis implements Runnable {

    private final Logger logger = Logger.getLogger(GuiLogger.class);
    private final Map<URI, List<URI>> graph;

    public BasicAnalysis(Map<URI, List<URI>> g) {
        graph = g;
    }

    @Override
    public void run() {
        String sb = "\n\n"
                + analyzeVerticesNumber(graph)
                + analyzeEdgesNumber(graph)
                + analyzeOutDegrees(graph)
                + analyzeInDegrees(graph);
        logger.result(sb);
    }

    static String analyzeVerticesNumber(Map<URI, List<URI>> graph) {
        int vertices = graph.size();
        return String.format("Number of vertices: %d%n%n", vertices);
    }

    static String analyzeEdgesNumber(Map<URI, List<URI>> graph) {
        int edges = graph.values().stream().mapToInt(List::size).sum();
        return String.format("Number of edges: %d%n%n", edges);
    }

    static String analyzeOutDegrees(Map<URI, List<URI>> graph) {
        StringBuilder sb = new StringBuilder();
        graph.values().stream()
                .mapToInt(List::size).boxed()
                .collect(Collectors.groupingBy(Integer::intValue, Collectors.counting()))
                .forEach((outDegree, count) -> sb.append(String.format("Vertices with out degree %d: %d%n", outDegree, count)));
        return sb.toString();
    }

    static String analyzeInDegrees(Map<URI, List<URI>> graph) {
        StringBuilder sb = new StringBuilder();
        Map<URI, Integer> degrees = calculateInDegrees(graph);

        // group entries by in-degree and prepare output
        degrees.entrySet().stream()
                .collect(Collectors.groupingBy(Map.Entry::getValue, Collectors.counting()))
                .forEach((inDegree, count) -> sb.append(String.format("Vertices with in degree %d: %d%n", inDegree, count)));

        return sb.toString();
    }

    static private Map<URI, Integer> calculateInDegrees(Map<URI, List<URI>> graph) {
        // initialize map with every URI with in-degree 0
        Map<URI, Integer> degrees = new HashMap<>();
        graph.keySet().forEach(uri -> degrees.put(uri, 0));

        // calculate in-degrees
        graph.values().stream().flatMap(Collection::stream)
                .forEach(e -> degrees.replace(e, degrees.get(e) + 1));
        return degrees;
    }
}
