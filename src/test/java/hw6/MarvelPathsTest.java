package hw6;

import hw3.EdgeTest;
import hw3.Graph;
import hw3.NodeTest;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class MarvelPathsTest {
    public static final String small_test = "src/main/java/hw6/data/staffSuperheroes.txt";
    public static final String small_test_malformed = "src/main/java/hw6/data/staffSuperheroes_malformed_version.txt";
    public static final Graph.Node Ernst = NodeTest.create("Ernst-the-Bicycling-Wizard");
    public static final Graph.Node Notkin = NodeTest.create("Notkin-of-the-Superhuman-Beard");
    public static final Graph.Node Perkins = NodeTest.create("Perkins-the-Magical-Singing-Instructor");
    public static final Graph.Node Grossman = NodeTest.create("Grossman-the-Youngest-of-them-all");

    private List<Graph.Node> nodes;

    @Before
    public void initialize() {
        nodes = new ArrayList<>();
        nodes.add(Ernst);
        nodes.add(Notkin);
        nodes.add(Perkins);
        nodes.add(Grossman);
    }

    /**
     * test additional behavior of loadData, specifically, if data is well-formed it should return a non-null grah.
     *                                                     if data is mal-formed it should return null.
     */
    @Test
    public void testLoadData() {
        assertNotNull(MarvelPaths.loadData(small_test));
        assertNull(MarvelPaths.loadData(small_test_malformed));
    }

    /**
     * test additional behavior of findPath, specifically, if it returns empty list if no path exist.
     */
    @Test
    public void testFindPath() {
        Graph graph = MarvelPaths.loadData(small_test);
        for (Graph.Node node : nodes) {
            assertEquals(0, MarvelPaths.findPath(graph, node, node).size());
        }
    }
}
