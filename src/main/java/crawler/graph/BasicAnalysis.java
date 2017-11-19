package crawler.graph;

import logger.GuiLogger;
import logger.Logger;

import java.net.URI;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BasicAnalysis implements Runnable {

    private final Logger logger = Logger.getLogger(GuiLogger.class);
    private final Map<URI, List<URI>> graph;

    public BasicAnalysis(Map<URI, List<URI>> g) {
        graph = g;
    }

    @Override
    public void run() {
        String sb = "\n\n"
                + analyzeVerticesNumber()
                + analyzeEdgesNumber()
                + analyzeOutDegrees()
                + analyzeInDegrees();
        logger.result(sb);
    }

    String analyzeVerticesNumber() {
        int vertices = graph.size();
        return String.format("Number of vertices: %d%n%n", vertices);
    }

    String analyzeEdgesNumber() {
        int edges = graph.values().stream().mapToInt(List::size).sum();
        return String.format("Number of edges: %d%n%n", edges);
    }

    String analyzeOutDegrees() {
        StringBuilder sb = new StringBuilder();
        graph.values().stream()
                .mapToInt(List::size).boxed()
                .collect(Collectors.groupingBy(Integer::intValue, Collectors.counting()))
                .forEach((outDegree, count) -> sb.append(String.format("Vertices with out degree %d: %d%n", outDegree, count)));
        return sb.toString();
    }

    String analyzeInDegrees() {
        StringBuilder sb = new StringBuilder();
        Map<URI, Integer> degrees = calculateInDegrees();

        // group entries by in-degree and prepare output
        degrees.entrySet().stream()
                .collect(Collectors.groupingBy(Map.Entry::getValue, Collectors.counting()))
                .forEach((inDegree, count) -> sb.append(String.format("Vertices with in degree %d: %d%n", inDegree, count)));

        return sb.toString();
    }

    private Map<URI, Integer> calculateInDegrees() {
        // initialize map with every URI with in-degree 0
        Map<URI, Integer> degrees = new HashMap<>();
        graph.keySet().forEach(uri -> degrees.put(uri, 0));

        // calculate in-degrees
        graph.values().stream().flatMap(Collection::stream)
                .forEach(e -> degrees.replace(e, degrees.get(e) + 1));
        return degrees;
    }
}
