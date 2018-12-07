package campuspaths.service;

import static org.junit.Assert.*;

import campuspaths.CampusPathsApplication;
import campuspaths.model.ServerSideException;
import hw8.CampusPath;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CampusPathsApplication.class)
public class CampusServiceTest {
  @Autowired private CampusService service;

  /** Test if listBuilding is implemented correctly. */
  @Test
  public void testListBuildings() {
    // Since listBuilding() is just a thin layer in CampusService,
    // i will just doing some sanity check.
    assertNotNull(service.listBuildings());
    assertEquals(service.listBuildings().size(), 51);
  }

  /**
   * Test uf findPath throws exception as expected. Specifically, if either of the parameter is not
   * a valid abbreviated building name
   */
  @Test(expected = ServerSideException.class)
  public void testFindPathException() throws ServerSideException {

    service.findPath(" ", "CSEE");
    service.findPath(" ", "");
    service.findPath("avc", "gggg");
  }

  /** Test if findPath is implemented correctly. */
  @Test
  public void testFindPath() {
    // Since findPath() is just another thin layer in CampusService,
    // plus the only additional functionality is tested in testFindPathException,
    // i will just do some sanity check below.
    try {
      List<CampusPath> paths;
      paths = service.findPath("CSE", "CSE");
      assertNotNull(paths);
      assertTrue(paths.isEmpty());
    } catch (ServerSideException e) {
      fail("unexpected exception");
    }
  }
}
