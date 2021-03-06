package campuspaths.api;

import campuspaths.model.ServerSideException;
import campuspaths.service.CampusService;
import UI.Building;
import UI.CampusPath;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** This class controls the Rest API for the Application */
@RestController
@CrossOrigin("http://localhost:3000")
public class PublicApi {
  // This is NOT An ADT!!!
  // This is NOT An ADT!!!
  // This is NOT An ADT!!!

  /** CampusService is the class that provides the main functionality of the back-end server. */
  @Autowired private CampusService service;

  /**
   * Find the path in the campus from the shortName of Start Building to the shortName of End
   * Building.
   *
   * <p>Note that this guarantees to find the shortest path.
   *
   * <p>A path consists of one or more CampusPath instances.
   *
   * <p>If multiple least weight path exists, it will return any of them. *
   *
   * <p>The path will be returned in the form of a list, where the first element is start
   * Building-Point1, * second element is Point1-Point2, ... the last element is Point(N-1) - end
   * Building. (N is the length of the list)
   *
   * <p>If such a path does not exist, it will return null.
   *
   * <p>For self-edge, it will return an empty list.
   *
   * @param start the short Name (abbreviated name) of Start Building
   * @param end the short Name (abbreviated name) of end Building
   * @return a list holding the path from Start Building to End Building if there exists one, or
   *     null otherwise.
   * @throws ServerSideException if short name of either building is invalid (i.e not found or does
   *     not have a corresponding full name)
   */
  @GetMapping("/findPath") // "http://localhost:8080/findPath?start=value&end=value"
  public List<CampusPath> findPath(
      @RequestParam(value = "start") String start, @RequestParam(value = "end") String end)
      throws ServerSideException {
    return service.findPath(start, end);
  }

  /**
   * List all Buildings in the campus.
   *
   * <p>It returns an unmodifiable view of all Buildings, each with its own short name, full name
   * and cost.
   *
   * @return an unmodifiable view of all Buildings, each with its own short name, full name and
   *     cost.
   */
  @GetMapping("/listBuilding") // "http://localhost:8080/listBuilding"
  public Set<Building> listBuildings() {
    return service.listBuildings();
  }
}
