package PathFinding;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import hw6.MarvelPathsTest;
import org.junit.Test;

public class MarvelPaths2Test extends MarvelPathsTest {
  public static final String MARVEL = "src/main/java/PathFinding/data/marvel.tsv";

  /**
   * test additional behavior of loadData, specifically, if data is well-formed it should return a
   * non-null grah. if data is mal-formed it should return null.
   */
  @Test
  public void testLoadData() {
    assertNotNull(MarvelPaths2.loadData(MARVEL));
    assertNotNull(MarvelPaths2.loadData(SMALL_TEST));
    assertNull(MarvelPaths2.loadData(SMALL_TEST_MALFORMED));
  }
}
