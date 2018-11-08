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
    public static final String SMALL_TEST = "src/main/java/hw6/data/staffSuperheroes.tsv";
    public static final String SMALL_TEST_MALFORMED = "src/main/java/hw6/data/staffSuperheroes_malformed_version.tsv";
    public static final String MARVEL = "src/main/java/hw6/data/marvel.tsv";
    public static final Graph.Node ERNST = NodeTest.create("Ernst-the-Bicycling-Wizard");
    public static final Graph.Node NOTKIN = NodeTest.create("Notkin-of-the-Superhuman-Beard");
    public static final Graph.Node PERKINS = NodeTest.create("Perkins-the-Magical-Singing-Instructor");
    public static final Graph.Node GROSSMAN = NodeTest.create("Grossman-the-Youngest-of-them-all");

    private List<Graph.Node> nodes;

    @Before
    public void initialize() {
        nodes = new ArrayList<>();
        nodes.add(ERNST);
        nodes.add(NOTKIN);
        nodes.add(PERKINS);
        nodes.add(GROSSMAN);
    }

    /**
     * test additional behavior of loadData, specifically, if data is well-formed it should return a non-null grah.
     *                                                     if data is mal-formed it should return null.
     */
    @Test
    public void testLoadData() {
        assertNotNull(MarvelPaths.loadData(MARVEL));
        assertNotNull(MarvelPaths.loadData(SMALL_TEST));
        assertNull(MarvelPaths.loadData(SMALL_TEST_MALFORMED));
    }

    /**
     * test additional behavior of findPath, specifically, if it returns empty list if no path exist.
     */
    @Test
    public void testFindPath() {
        Graph graph = MarvelPaths.loadData(SMALL_TEST);
        for (Graph.Node node : nodes) {
            assertEquals(0, MarvelPaths.findPath(graph, node, node).size());
        }
    }


}
