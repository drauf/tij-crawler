package crawler.graph;

import logger.GuiLogger;
import logger.Logger;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

public class BasicAnalysis implements Callable<Object> {

    private final Logger logger = Logger.getLogger(GuiLogger.class);
    private final ConcurrentMap<String, List<String>> graph;

    public BasicAnalysis(ConcurrentMap<String, List<String>> g) {
        graph = g;
    }

    @Override
    public Object call() throws Exception {
        StringBuilder sb = new StringBuilder("\n\n");
        analyzeVerticesNumber(sb);
        analyzeEdgesNumber(sb);
        analyzeOutDegrees(sb);
        analyzeInDegrees(sb);
        logger.info(sb.toString());
        return null;
    }

    private void analyzeVerticesNumber(StringBuilder sb) {
        int vertices = graph.size();
        sb.append(String.format("Number of vertices: %d\n\n", vertices));
    }

    private void analyzeEdgesNumber(StringBuilder sb) {
        int edges = graph.values().stream().mapToInt(List::size).sum();
        sb.append(String.format("Number of edges: %d\n\n", edges));
    }

    private void analyzeOutDegrees(StringBuilder sb) {
        graph.values().stream().mapToInt(List::size).boxed()
                .collect(Collectors.groupingBy(Integer::intValue, Collectors.counting()))
                .forEach((outDegree, count) -> sb.append(String.format("Vertices with out degree %d: %d\n", outDegree, count)));
        sb.append("\n");
    }

    private void analyzeInDegrees(StringBuilder sb) {
        graph.entrySet().stream()
                .collect(Collectors.groupingBy(Map.Entry::getKey, Collectors.counting()))
                .values().stream().mapToInt(Long::intValue).boxed()
                .collect(Collectors.groupingBy(Integer::intValue, Collectors.counting()))
                .forEach((inDegree, count) -> sb.append(String.format("Vertices with in degree %d: %d\n", inDegree, count)));
    }
}
