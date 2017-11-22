package crawler.graph;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class StronglyConnectedComponentsAnalysisTest {

    private Map<URI, List<URI>> graph;

    @BeforeEach
    void init() {
        graph = new HashMap<>();
    }

    @Test
    void getStronglyConnectedComponents_emptyMap_returnEmptyList() throws URISyntaxException {
        List<List<StronglyConnectedComponentsAnalysis.Vertex>> result = StronglyConnectedComponentsAnalysis.getStronglyConnectedComponents(graph);

        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    void getStronglyConnectedComponents_nonEmptyMap_returnSCC() throws URISyntaxException {
        fillGraph();

        List<List<StronglyConnectedComponentsAnalysis.Vertex>> result = StronglyConnectedComponentsAnalysis.getStronglyConnectedComponents(graph);

        assertThat(result.size()).isEqualTo(4);
        assertThat(result.get(0).size()).isEqualTo(3);
        assertThat(result.get(1).size()).isEqualTo(2);
        assertThat(result.get(2).size()).isEqualTo(2);
        assertThat(result.get(3).size()).isEqualTo(1);
    }

    private void fillGraph() throws URISyntaxException {
        graph.put(new URI("1"), Collections.singletonList(new URI("2")));
        graph.put(new URI("2"), Collections.singletonList(new URI("3")));
        graph.put(new URI("3"), Collections.singletonList(new URI("1")));
        graph.put(new URI("4"), Arrays.asList(new URI("2"), new URI("3"), new URI("5")));
        graph.put(new URI("5"), Arrays.asList(new URI("4"), new URI("6")));
        graph.put(new URI("6"), Arrays.asList(new URI("3"), new URI("7")));
        graph.put(new URI("7"), Collections.singletonList(new URI("6")));
        graph.put(new URI("8"), Arrays.asList(new URI("5"), new URI("7"), new URI("8")));
    }
}
