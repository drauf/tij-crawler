package crawler.graph;

import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static crawler.graph.StronglyConnectedComponentsAnalysis.Vertex;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class StronglyConnectedComponentsAnalysisTest {

    @Test
    void getStronglyConnectedComponents_emptyMap_returnEmptyList() throws URISyntaxException {
        Map<URI, List<URI>> graph = getEmptyGraph();

        List<List<Vertex>> result = StronglyConnectedComponentsAnalysis.getStronglyConnectedComponents(graph);

        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    void getStronglyConnectedComponents_nonEmptyMap_returnSCC() throws URISyntaxException {
        Map<URI, List<URI>> graph = getFilledGraph();

        List<List<Vertex>> result = StronglyConnectedComponentsAnalysis.getStronglyConnectedComponents(graph);

        assertThat(result.size()).isEqualTo(4);
        assertThat(result.get(0).size()).isEqualTo(3);
        assertThat(result.get(1).size()).isEqualTo(2);
        assertThat(result.get(2).size()).isEqualTo(2);
        assertThat(result.get(3).size()).isEqualTo(1);
    }

    @Test
    void calculateDistancesInsideComponents_emptyScc_returnEmptyMatrix() throws URISyntaxException {
        List<List<Vertex>> scc = getEmptySCC();

        List<int[][]> result = StronglyConnectedComponentsAnalysis.calculateDistancesInsideComponents(scc);

        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    void calculateDistancesInsideComponents_nonEmptyScc_returnMatrixOfDistances() throws URISyntaxException {
        List<List<Vertex>> scc = getSCC();

        List<int[][]> result = StronglyConnectedComponentsAnalysis.calculateDistancesInsideComponents(scc);

        assertThat(result.size()).isEqualTo(4);
        assertThat(result.get(0).length).isEqualTo(3);
        assertThat(result.get(1).length).isEqualTo(2);
        assertThat(result.get(2).length).isEqualTo(2);
        assertThat(result.get(3).length).isEqualTo(1);
    }

    private Map<URI, List<URI>> getEmptyGraph() {
        return new HashMap<>();
    }

    private Map<URI, List<URI>> getFilledGraph() throws URISyntaxException {
        Map<URI, List<URI>> graph = new HashMap<>();
        graph.put(new URI("1"), Collections.singletonList(new URI("2")));
        graph.put(new URI("2"), Collections.singletonList(new URI("3")));
        graph.put(new URI("3"), Collections.singletonList(new URI("1")));
        graph.put(new URI("4"), Arrays.asList(new URI("2"), new URI("3"), new URI("5")));
        graph.put(new URI("5"), Arrays.asList(new URI("4"), new URI("6")));
        graph.put(new URI("6"), Arrays.asList(new URI("3"), new URI("7")));
        graph.put(new URI("7"), Collections.singletonList(new URI("6")));
        graph.put(new URI("8"), Arrays.asList(new URI("5"), new URI("7"), new URI("8")));
        return graph;
    }

    private List<List<Vertex>> getEmptySCC() throws URISyntaxException {
        return new ArrayList<>();
    }

    private List<List<Vertex>> getSCC() throws URISyntaxException {
        Vertex v1 = new Vertex(1, 1);
        Vertex v2 = new Vertex(2, 1);
        Vertex v3 = new Vertex(3, 1);
        v1.edges.add(v2);
        v2.edges.add(v3);
        v3.edges.add(v1);

        Vertex v4 = new Vertex(4, 4);
        Vertex v5 = new Vertex(5, 4);
        v4.edges.add(v5);
        v5.edges.add(v4);

        Vertex v6 = new Vertex(6, 6);
        Vertex v7 = new Vertex(7, 6);
        Vertex v8 = new Vertex(8, 8);
        v6.edges.add(v7);
        v7.edges.add(v6);

        List<List<Vertex>> scc = new ArrayList<>();
        scc.add(Arrays.asList(v1, v2, v3));
        scc.add(Arrays.asList(v4, v5));
        scc.add(Arrays.asList(v6, v7));
        scc.add(Collections.singletonList(v8));
        return scc;
    }
}
