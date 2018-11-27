package hw8;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class CampusMapModelTest {

  public static String buildingsFileName = "src/main/java/hw8/data/campus_buildings_test.tsv";
  public static String buildingsMalFormedFileName =
      "src/main/java/hw8/data/campus_buildings_malformed.tsv";
  public static String pathsFileName = "src/main/java/hw8/data/campus_paths_test.tsv";
  public static String pathsMalFormedFileName = "src/main/java/hw8/data/campus_paths_malformed.tsv";

  public List<Coordinate> buildings;

  public List<CampusPath> from00To11;

  public List<CampusPath> from22To33;

  @Before
  public void initialize() {
    buildings = new ArrayList<>();
    buildings.add(new Coordinate(0, 0));
    buildings.add(new Coordinate(1, 1));
    buildings.add(new Coordinate(2, 2));
    buildings.add(new Coordinate(3, 3));

    from00To11 = new ArrayList<>();
    from00To11.add(new CampusPath(new Coordinate(0, 0), new Coordinate(0.9, 0.9), 0.1));
    from00To11.add(new CampusPath(new Coordinate(0.9, 0.9), new Coordinate(1, 1), 0.1));

    from22To33 = new ArrayList<>();
    from22To33.add(new CampusPath(new Coordinate(2, 2), new Coordinate(2.8, 2.8), 0.1));
    from22To33.add(new CampusPath(new Coordinate(2.8, 2.8), new Coordinate(3, 3), 0.1));
  }

  /**
   * Test makeInstance is implemented properly. Specifically, testPathAllBuildings.test if buildings
   * data and path data are loaded correctly.
   */
  @Test
  public void testMakeInstance() {
    // Test if all buildings are ready.
    CampusMapModel model = CampusMapModel.makeInstance(pathsFileName, buildingsFileName);

    // If these two sets are equal and one is the subset of another, then they must be the same set.
    assertEquals(model.listBuildings().size(), buildings.size());
    for (Building building : model.listBuildings()) {
      assertTrue(buildings.contains(building.getLocation()));
    }

    // Test if all paths are added.
    // Especially testPathAllBuildings.test if nodes other than the location of buildings are added.
    assertEquals(from00To11, model.findPath(new Coordinate(0, 0), new Coordinate(1, 1)));
    assertEquals(from22To33, model.findPath(new Coordinate(2, 2), new Coordinate(3, 3)));
  }

  /**
   * Test if IOException or DataMalformed Exception are correctly detected and rethrow as
   * RuntimeException.
   */
  @Test(expected = RuntimeException.class)
  public void testMakeInstanceException() {
    // Note that all exceptions are chained into
    // Test if malformed exception is expected.
    CampusMapModel.makeInstance(buildingsMalFormedFileName, pathsFileName);

    // Test if IO exception is expected.
    CampusMapModel.makeInstance(buildingsFileName, pathsMalFormedFileName);
  }
}
