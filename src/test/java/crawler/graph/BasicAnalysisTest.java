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

class BasicAnalysisTest {

    private Map<URI, List<URI>> graph;

    private static final String ANALYZE_VERTICES_FORMAT = "Number of vertices: %d%n%n";
    private static final String ANALYZE_EDGES_FORMAT = "Number of edges: %d%n%n";
    private static final String ANALYZE_OUT_DEGREES_FORMAT = "Vertices with out degree %d: %d%n";
    private static final String ANALYZE_IN_DEGREES_FORMAT = "Vertices with in degree %d: %d%n";

    @BeforeEach
    void init() {
        graph = new HashMap<>();
    }

    @Test
    void analyzeVerticesNumber_emptyMap_ReturnZero() throws URISyntaxException {
        String result = BasicAnalysis.analyzeVerticesNumber(graph);

        assertThat(result).isEqualTo(String.format(ANALYZE_VERTICES_FORMAT, 0));
    }

    @Test
    void analyzeVerticesNumber_nonEmptyMap_ReturnNumberOfEntries() throws URISyntaxException {
        fillGraph();

        String result = BasicAnalysis.analyzeVerticesNumber(graph);

        assertThat(result).isEqualTo(String.format(ANALYZE_VERTICES_FORMAT, 5));
    }

    @Test
    void analyzeEdgesNumber_emptyMap_ReturnZero() throws URISyntaxException {
        String result = BasicAnalysis.analyzeEdgesNumber(graph);

        assertThat(result).isEqualTo(String.format(ANALYZE_EDGES_FORMAT, 0));
    }

    @Test
    void analyzeEdgesNumber_nonEmptyMap_ReturnNumberOfValues() throws URISyntaxException {
        fillGraph();

        String result = BasicAnalysis.analyzeEdgesNumber(graph);

        assertThat(result).isEqualTo(String.format(ANALYZE_EDGES_FORMAT, 7));
    }

    @Test
    void analyzeOutDegrees_emptyMap_ReturnEmptyString() throws URISyntaxException {
        String result = BasicAnalysis.analyzeOutDegrees(graph);

        assertThat(result).isEqualTo("");
    }

    @Test
    void analyzeOutDegrees_nonEmptyMap_CalculateOutDegreesCorrectly() throws URISyntaxException {
        fillGraph();

        String result = BasicAnalysis.analyzeOutDegrees(graph);

        String expectedResult = String.format(ANALYZE_OUT_DEGREES_FORMAT, 0, 2)
                + String.format(ANALYZE_OUT_DEGREES_FORMAT, 1, 1)
                + String.format(ANALYZE_OUT_DEGREES_FORMAT, 2, 1)
                + String.format(ANALYZE_OUT_DEGREES_FORMAT, 4, 1);
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void analyzeInDegrees_emptyMap_ReturnEmptyString() throws URISyntaxException {
        String result = BasicAnalysis.analyzeInDegrees(graph);

        assertThat(result).isEqualTo("");
    }

    @Test
    void analyzeInDegrees_nonEmptyMap_CalculateInDegreesCorrectly() throws URISyntaxException {
        fillGraph();

        String result = BasicAnalysis.analyzeInDegrees(graph);

        String expectedResult = String.format(ANALYZE_IN_DEGREES_FORMAT, 0, 3)
                + String.format(ANALYZE_IN_DEGREES_FORMAT, 2, 1)
                + String.format(ANALYZE_IN_DEGREES_FORMAT, 5, 1);
        assertThat(result).isEqualTo(expectedResult);
    }

    private void fillGraph() throws URISyntaxException {
        graph.put(new URI("1"), Collections.singletonList(new URI("1")));
        graph.put(new URI("2"), Collections.emptyList());
        graph.put(new URI("3"), Arrays.asList(new URI("1"), new URI("1"), new URI("1"), new URI("1")));
        graph.put(new URI("4"), Collections.emptyList());
        graph.put(new URI("5"), Arrays.asList(new URI("4"), new URI("4")));
    }
}
