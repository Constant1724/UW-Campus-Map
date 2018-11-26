package hw6;

import static org.junit.Assert.*;

import hw3.Graph;
import hw3.NodeTest;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class MarvelPathsTest {
  public static final String SMALL_TEST = "src/main/java/hw6/data/staffSuperheroes.tsv";
  public static final String SMALL_TEST_MALFORMED =
      "src/main/java/hw6/data/staffSuperheroes_malformed_version.tsv";
  public static final String MARVEL = "src/main/java/hw6/data/marvel.tsv";
  public static final Graph<String, String>.Node ERNST =
      NodeTest.create("Ernst-the-Bicycling-Wizard");
  public static final Graph<String, String>.Node NOTKIN =
      NodeTest.create("Notkin-of-the-Superhuman-Beard");
  public static final Graph<String, String>.Node PERKINS =
      NodeTest.create("Perkins-the-Magical-Singing-Instructor");
  public static final Graph<String, String>.Node GROSSMAN =
      NodeTest.create("Grossman-the-Youngest-of-them-all");

  private List<Graph<String, String>.Node> nodes;

  @Before
  public void initialize() {
    nodes = new ArrayList<>();
    nodes.add(ERNST);
    nodes.add(NOTKIN);
    nodes.add(PERKINS);
    nodes.add(GROSSMAN);
  }

  /**
   * testPathAllBuildings.test additional behavior of loadData, specifically, if data is well-formed it should return a
   * non-null grah. if data is mal-formed it should return null.
   */
  @Test
  public void testLoadData() {
    assertNotNull(MarvelPaths.loadData(MARVEL));
    assertNotNull(MarvelPaths.loadData(SMALL_TEST));
    assertNull(MarvelPaths.loadData(SMALL_TEST_MALFORMED));
  }

  /**
   * testPathAllBuildings.test additional behavior of findPath, specifically, if it returns empty list for self edge and
   * returns null for no path exists.
   */
  @Test
  public void testFindPath() {
    Graph<String, String> graph = new Graph<String, String>();
    for (Graph<String, String>.Node node1 : nodes) {
      graph.addNode(node1);
      for (Graph<String, String>.Node node2 : nodes) {
        if (!node1.equals(node2)) {
          // check no path case
          assertNull(MarvelPaths.findPath(graph, node1, node2));
        } else {
          // check self-edge case
          assertEquals(0, MarvelPaths.findPath(graph, node1, node2).size());
        }
      }
    }
  }
}
