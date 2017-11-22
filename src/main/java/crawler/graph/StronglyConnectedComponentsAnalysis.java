package crawler.graph;

import logger.GuiLogger;
import logger.Logger;

import java.net.URI;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Collectors;

public class StronglyConnectedComponentsAnalysis implements Runnable {

    private final Logger logger = Logger.getLogger(GuiLogger.class);
    private final Map<URI, List<URI>> graph;
    private static int index;

    static class Vertex {
        private int index;
        private int lowLink;
        private boolean onStack;
        List<Vertex> edges;

        private Vertex() {
            edges = new ArrayList<>();
        }

        Vertex(int index, int lowLink) {
            this.index = index;
            this.lowLink = lowLink;
            edges = new ArrayList<>();
        }
    }

    public StronglyConnectedComponentsAnalysis(Map<URI, List<URI>> g) {
        graph = g;
    }

    @Override
    public void run() {
        List<List<Vertex>> components = getStronglyConnectedComponents(graph);
        List<int[][]> distances = calculateDistancesInsideComponents(components);
        // calculate average distance and diameter of every component (the biggest distance between vertices)
    }

    // Tarjan's strongly connected components algorithm - O(|V| + |E|)
    // https://en.wikipedia.org/wiki/Tarjan%27s_strongly_connected_components_algorithm
    static List<List<Vertex>> getStronglyConnectedComponents(Map<URI, List<URI>> graph) {
        index = 1;
        List<List<Vertex>> stronglyConnectedComponents = new ArrayList<>();
        Queue<Vertex> stack = Collections.asLifoQueue(new ArrayDeque<Vertex>());
        Vertex[] vertices = graphToVertexArray(graph);

        // build strongly connected components
        for (Vertex vertex : vertices) {
            if (vertex.index == 0) {
                strongConnect(vertex, stack,stronglyConnectedComponents);
            }
        }

        // filter out edges to other components
        for (Vertex vertex : vertices) {
            vertex.edges = vertex.edges.stream()
                    .filter(e -> e.lowLink == vertex.lowLink)
                    .collect(Collectors.toList());
        }

        return stronglyConnectedComponents;
    }

    static private Vertex[] graphToVertexArray(Map<URI, List<URI>> graph) {
        Map<URI, Vertex> uriVertexMap = new HashMap<>();
        // create map containing all vertices but no edges
        graph.keySet().forEach(key -> uriVertexMap.put(key, new Vertex()));
        // add edges to the map
        graph.forEach((u, listOfW) -> listOfW.forEach(w ->
                uriVertexMap.get(u).edges.add(uriVertexMap.get(w))));
        return uriVertexMap.values().toArray(new Vertex[0]);
    }

    static private void strongConnect(Vertex currentVertex, Queue<Vertex> stack, List<List<Vertex>> stronglyConnectedComponents) {
        currentVertex.index = index;
        currentVertex.lowLink = index;
        index++;
        stack.add(currentVertex);
        currentVertex.onStack = true;

        for (Vertex successor : currentVertex.edges) {
            if (successor.index == 0) {
                // successor has not yet been visited; recurse on it
                strongConnect(successor, stack, stronglyConnectedComponents);
                currentVertex.lowLink = Math.min(currentVertex.lowLink, successor.lowLink);
            } else if (successor.onStack) {
                // successor is in stack and hence in the current strongly connected component
                currentVertex.lowLink = Math.min(currentVertex.lowLink, successor.index);
            }
        }

        // if currentVertex is a root node, pop the stack and generate a strongly connected component
        if (currentVertex.lowLink == currentVertex.index) {
            List<Vertex> scc = new ArrayList<>();
            Vertex other;
            do {
                other = stack.remove();
                other.onStack = false;
                scc.add(other);
            } while (other != currentVertex);
            stronglyConnectedComponents.add(scc);
        }
    }

    // Floyd-Warshall algorithm - O(|V|^3)
    // https://en.wikipedia.org/wiki/Floyd%E2%80%93Warshall_algorithm
    static List<int[][]> calculateDistancesInsideComponents(List<List<Vertex>> components) {
        List<int[][]> results = new ArrayList<>();
        for (List<Vertex> vertices : components) {
            final int verticesNo = vertices.size();
            final int distances[][] = createMatrix(verticesNo);

            for (Vertex vertex : vertices) {
                int currentIndex = vertex.index - vertex.lowLink;
                vertex.edges.forEach(e -> distances[currentIndex][e.index - e.lowLink] = 1);
            }

            for (int k = 0; k < verticesNo; k++) {
                for (int i = 0; i < verticesNo; i++) {
                    for (int j = 0; j < verticesNo; j++) {
                        if (distances[i][j] > distances[i][k] + distances[k][j]) {
                            distances[i][j] = distances[i][k] + distances[k][j];
                        }
                    }
                }
            }

            results.add(distances);
        }
        return results;
    }

    private static int[][] createMatrix(int size) {
        final int matrix[][] = new int[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix[i][j] = 2000000;
            }
            matrix[i][i] = 0;
        }

        return matrix;
    }
}
